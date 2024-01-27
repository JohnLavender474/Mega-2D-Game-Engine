package com.engine.world

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedSet
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.graph.IGraphMap
import com.engine.systems.GameSystem
import kotlin.math.abs

/**
 * A system that handles the physics of the game. This system is responsible for updating the
 * positions of all bodies, resolving collisions, and notifying the [IContactListener] of any
 * contacts that occur. This system is stateful. This system processes entities that have a
 * [BodyComponent].
 *
 * This system uses a [IGraphMap] to determine which bodies are overlapping. The [IGraphMap] is
 * nullable but must not be null when the system is run. The [IGraphMap] is nullable simply to not
 * throw a null pointer exception if [reset] is called and the [IGraphMap] is null.
 *
 * This system uses a [IContactListener] to notify of contacts.
 *
 * This system uses a [ICollisionHandler] to resolve collisions.
 *
 * This system uses a [fixedStep] to update the physics. If the delta time during an update is
 * greater than the fixed step, the physics will possibly be updated multiple times in one update
 * cycle. On the other hand, if the delta time during an update is less than the fixed step, then
 * possibly there won't be a physic update during that update.
 *
 * Keep in mind that different values for the fixed step will result in different physics outcomes.
 * It is up to the user to determine what the best value for the fixed step is. I personally
 * recommend the fixed step be half the target FPS. More physics updates per frame will result in
 * more accurate and reliable physics, but will also result in more processing time.
 *
 * This system uses an optional [contactFilterMap] to optimize the determination of which contacts
 * to notify the [IContactListener] of. The [contactFilterMap] can be used to filter only cases
 * where the user has an implementation for two fixtures. For example, let's say there's a contact
 * between one fixture with type "Type1" and another fixture with type "Type2". If there's no
 * implementation in the provided [IContactListener] for the case of "Type1" touching "Type2", then
 * by not providing the entry in the [contactFilterMap], the [IContactListener] will NOT be
 * notified. The order of the entry is not important, so using "Type1" as the key with "Type2"
 * inside the value [Set<String>], or else vice versa, will NOT change the result. If the
 * [contactFilterMap] is null, then the [IContactListener] will be notified of ALL contacts.
 *
 * The [preProcess] function is run for each [Body] directly after the body's own [Body.preProcess]
 * function has been run. This is useful if you want to run common logic for all bodies in the
 * pre-processing stage.
 *
 * The [postProcess] function is run for each [Body] directly after the body's own
 * [Body.postProcess] function has been run. This is useful if you want to run common logic for all
 * bodies in the post-processing stage.
 *
 * In regard to fixture overlaps, if [Fixture.attachedToBody] is true for a fixture, then its
 * [Fixture.bodyRelativeShape] is used; otherwise its [Fixture.shape] is used. If the fixture is
 * attached to a body but its [Fixture.bodyRelativeShape] is null, then an exception will be thrown.
 */
class WorldSystem(
    private val contactListener: IContactListener,
    private val worldGraphSupplier: () -> IGraphMap?,
    private val fixedStep: Float,
    private val collisionHandler: ICollisionHandler = StandardCollisionHandler,
    private val contactFilterMap: ObjectMap<Any, ObjectSet<Any>>? = null
) : GameSystem(BodyComponent::class) {

  internal var priorContactSet = OrderedSet<Contact>()
  internal var currentContactSet = OrderedSet<Contact>()
  internal var accumulator = 0f

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    accumulator += delta
    while (accumulator >= fixedStep) {
      accumulator -= fixedStep
      cycle(entities, fixedStep)
    }
  }

  override fun reset() {
    super.reset()
    accumulator = 0f
    priorContactSet.clear()
    currentContactSet.clear()
    worldGraphSupplier()?.reset()
  }

  /**
   * Cycles through the entities. This method is called by the [process] method. This method is
   * responsible for updating the positions of all bodies, resolving collisions, and notifying the
   * [IContactListener] of any contacts that occur.
   *
   * @param entities the [Collection] of [IGameEntity]s to process
   * @param delta the time in seconds since the last frame
   */
  internal fun cycle(entities: ImmutableCollection<IGameEntity>, delta: Float) {
    preProcess(entities, delta)
    worldGraphSupplier()!!.reset()
    entities.forEach { processPhysicsAndGraph(it, delta) }
    entities.forEach { processContactsAndCollisions(it) }
    processContacts()
    postProcess(entities, delta)
  }

  /**
   * Pre-processes the entities. This method is called by the [cycle] method. This method is
   * responsible for resetting the [IGraphMap] and the [PhysicsData] of all bodies, and updating the
   * [Updatable]s of all bodies.
   *
   * @param entities the [Collection] of [IGameEntity]s to process
   * @param delta the time in seconds since the last frame
   */
  internal fun preProcess(entities: ImmutableCollection<IGameEntity>, delta: Float) {
    entities.forEach { entity ->
      entity.getComponent(BodyComponent::class)?.body?.let { body ->
        body.previousBounds.set(body.rotatedBounds)
        body.preProcess?.update(delta)
      }
    }
  }

  /**
   * Processes the physics and graph for the given entity.
   *
   * @param entity the entity to process
   * @param delta the time in seconds since the last frame
   */
  internal fun processPhysicsAndGraph(entity: IGameEntity, delta: Float) {
    val worldGraph =
        worldGraphSupplier() ?: throw IllegalStateException("World graph cannot be null.")

    entity.getComponent(BodyComponent::class)?.body?.let { body ->
      updatePhysics(body, delta)
      updateFixturePositions(body)

      worldGraph.add(body, body.rotatedBounds)

      body.fixtures.forEach { (_, fixture) ->
        val shape =
            if (fixture.attachedToBody)
                fixture.bodyRelativeShape
                    ?: throw IllegalStateException(
                        "Fixture is attached to body but body relative shape is null. " +
                            "Fixture: $fixture. Entity: $entity")
            else fixture.shape

        worldGraph.add(fixture, shape)
      }
    }
  }

  /**
   * Processes the contacts and collisions for the given entity.
   *
   * @param entity the entity to process
   */
  internal fun processContactsAndCollisions(entity: IGameEntity) {
    entity.getComponent(BodyComponent::class)?.body?.let { body ->
      checkForContacts(body)
      resolveCollisions(body)
    }
  }

  /**
   * Post-processes the entities. This method is called by the [cycle] method. This method is
   * responsible for notifying the [IContactListener] of any contacts that occur.
   *
   * @param entities the [Collection] of [IGameEntity]s to process
   * @param delta the time in seconds since the last frame
   */
  internal fun postProcess(entities: ImmutableCollection<IGameEntity>, delta: Float) {
    entities.forEach { entity ->
      entity.getComponent(BodyComponent::class)?.body?.let { body ->
        body.postProcess?.update(delta)
      }
    }
  }

  /** Processes and updates the [Contact] sets. */
  internal fun processContacts() {
    currentContactSet.forEach {
      if (priorContactSet.contains(it)) contactListener.continueContact(it, fixedStep)
      else contactListener.beginContact(it, fixedStep)
    }

    priorContactSet.forEach {
      if (!currentContactSet.contains(it)) contactListener.endContact(it, fixedStep)
    }

    priorContactSet = currentContactSet
    currentContactSet = OrderedSet()
  }

  /**
   * Updates the physics of the given body.
   *
   * @param body the [Body] to update the physics of
   * @param delta the time in seconds since the last frame
   */
  internal fun updatePhysics(body: Body, delta: Float) {
    body.physics.let {
      if (it.takeFrictionFromOthers) {
        if (it.frictionOnSelf.x > 0f) it.velocity.x /= it.frictionOnSelf.x
        if (it.frictionOnSelf.y > 0f) it.velocity.y /= it.frictionOnSelf.y
      }

      it.frictionOnSelf.set(it.defaultFrictionOnSelf)

      if (it.gravityOn) it.velocity.add(it.gravity)

      // clamp the x velocity
      if (it.velocity.x > 0f && it.velocity.x > abs(it.velocityClamp.x))
          it.velocity.x = abs(it.velocityClamp.x)
      else if (it.velocity.x < 0f && it.velocity.x < -abs(it.velocityClamp.x))
          it.velocity.x = -abs(it.velocityClamp.x)

      // clamp the y velocity
      if (it.velocity.y > 0f && it.velocity.y > abs(it.velocityClamp.y))
          it.velocity.y = abs(it.velocityClamp.y)
      else if (it.velocity.y < 0f && it.velocity.y < -abs(it.velocityClamp.y))
          it.velocity.y = -abs(it.velocityClamp.y)

      body.x += it.velocity.x * delta
      body.y += it.velocity.y * delta
    }
  }

  /**
   * Updates the positions of the given body's fixtures.
   *
   * @param body the [Body] to update the positions of the fixtures of
   */
  internal fun updateFixturePositions(body: Body) {
    body.fixtures.forEach { (_, fixture) ->
      if (!fixture.attachedToBody) return@forEach
      fixture.setBodyRelativeShape(body)
    }
  }

  /**
   * Filters the given fixtures. This method is called by the [resolveCollisions] method. This
   * method is responsible for filtering the given fixtures based on the [contactFilterMap]. If the
   * [contactFilterMap] is null, then this method will return true. Otherwise, this method will
   * return true if the [contactFilterMap] contains the given fixtures.
   *
   * @param fixture1 the first [Fixture] to filter
   * @param fixture2 the second [Fixture] to filter
   * @return whether the given fixtures should be filtered
   */
  internal fun filterContact(fixture1: Fixture, fixture2: Fixture) =
      (fixture1 != fixture2) &&
          (contactFilterMap?.get(fixture1.fixtureLabel)?.contains(fixture2.fixtureLabel) != false ||
              contactFilterMap[fixture2.fixtureLabel]?.contains(fixture1.fixtureLabel) != false)

  /**
   * Checks for contacts with the given body.
   *
   * @param body the [Body] to check for contacts
   */
  internal fun checkForContacts(body: Body) {
    val worldGraph =
        worldGraphSupplier() ?: throw IllegalStateException("World graph cannot be null.")

    body.fixtures.forEach { (_, fixture) ->
      if (fixture.active && contactFilterMap?.containsKey(fixture.fixtureLabel) != false) {
        val overlapping = ObjectSet<Fixture>()

        val shape =
            if (fixture.attachedToBody)
                fixture.bodyRelativeShape
                    ?: throw IllegalStateException(
                        "Fixture is attached to body but body relative shape is null. " +
                            "Fixture: $fixture. Entity: $body")
            else fixture.shape

        worldGraph.get(shape).filterIsInstance<Fixture>().forEach {
          if (it.active && filterContact(fixture, it) && fixture.overlaps(it)) overlapping.add(it)
        }

        overlapping.forEach { o -> currentContactSet.add(Contact(fixture, o)) }
      }
    }
  }

  /**
   * Resolves the collisions of the given body. This method is responsible for resolving the
   * collisions of the given body and notifying the [ICollisionHandler] of any collisions that
   * occur.
   *
   * @param body the [Body] to resolve the collisions of
   */
  internal fun resolveCollisions(body: Body) {
    worldGraphSupplier()!!.get(body.rotatedBounds).filterIsInstance<Body>().forEach {
      if (it != body && it.rotatedBounds.overlaps(body as Rectangle))
          collisionHandler.handleCollision(body, it)
    }
  }
}
