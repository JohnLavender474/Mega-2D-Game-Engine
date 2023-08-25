package com.engine.world

import com.badlogic.gdx.utils.OrderedSet
import com.engine.Entity
import com.engine.GameSystem
import com.engine.common.objects.DataWrapper
import kotlin.math.abs

/**
 * A system that handles the physics of the game. This system is responsible for updating the
 * positions of all bodies, resolving collisions, and notifying the [ContactListener] of any
 * contacts that occur. This system is stateful. This system processes entities that have a
 * [BodyComponent]. This system uses a [WorldGraph] to determine which bodies are overlapping.
 *
 * This system uses a [ContactListener] to notify of contacts.
 *
 * This system uses a [CollisionHandler] to resolve collisions.
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
 * to notify the [ContactListener] of. The [contactFilterMap] can be used to filter only cases where
 * the user has an implementation for two fixtures. For example, let's say there's a contact between
 * one fixture with type "Type1" and another fixture with type "Type2". If there's no implementation
 * in the provided [ContactListener] for the case of "Type1" touching "Type2", then by not providing
 * the entry in the [contactFilterMap], the [ContactListener] will NOT be notified. The order of the
 * entry is not important, so using "Type1" as the key with "Type2" inside the value [Set<String>],
 * or else vice versa, will NOT change the result. If the [contactFilterMap] is null, then the
 * [ContactListener] will be notified of ALL contacts.
 */
class WorldSystem(
    private val contactListener: ContactListener,
    private val worldGraph: DataWrapper<WorldGraph>,
    private val fixedStep: Float,
    private val collisionHandler: CollisionHandler = StandardCollisionHandler,
    private val contactFilterMap: Map<String, Set<String>>? = null,
) : GameSystem(listOf(BodyComponent::class)) {

  companion object {
    private const val PROCESS_CYCLES = 2
  }

  private var priorContactSet = OrderedSet<Contact>()
  private var currentContactSet = OrderedSet<Contact>()

  private var accumulator = 0f

  /**
   * Processes the entities. This method is called by the [GameSystem] when it is time for
   * processing. This method is responsible for updating the positions of all bodies, resolving
   * collisions, and notifying the [ContactListener] of any contacts that occur.
   *
   * @param on whether this [GameSystem] is on
   * @param entities the [ArrayList] of [Entity]s to process
   * @param delta the time in seconds since the last frame
   * @see GameSystem.process
   */
  override fun process(on: Boolean, entities: ArrayList<Entity>, delta: Float) {
    if (!on) {
      return
    }
    accumulator += delta
    while (accumulator >= fixedStep) {
      accumulator -= fixedStep
      var currentCycle = 0
      preProcess(entities, delta)
      while (currentCycle < PROCESS_CYCLES) {
        entities.forEach { processEntity(it, currentCycle, delta) }
        currentCycle++
      }
      postProcess(entities, delta)
    }
  }

  private fun preProcess(entities: ArrayList<Entity>, delta: Float) {
    worldGraph.data.reset()
    entities.forEach {
      val body = it.getComponent(BodyComponent::class).body
      body.previousBounds.set(body)
      body.preProcess?.update(delta)
    }
  }

  private fun processEntity(entity: Entity, currentCycle: Int, delta: Float) {
    val body: Body = entity.getComponent(BodyComponent::class).body
    when (currentCycle) {
      0 -> updatePhysics(body, delta)
      1 -> resolveContacts(body)
    }
  }

  private fun postProcess(entities: ArrayList<Entity>, delta: Float) {
    entities.forEach { e ->
      val body = e.getComponent(BodyComponent::class).body
      worldGraph.data.addBody(body)
      body.fixtures.forEach { f -> worldGraph.data.addFixture(f) }
      body.postProcess?.update(delta)
    }
    currentContactSet.forEach {
      if (priorContactSet.contains(it)) {
        contactListener.continueContact(it.fixture1, it.fixture2, fixedStep)
      } else {
        contactListener.beginContact(it.fixture1, it.fixture2, fixedStep)
      }
    }
    priorContactSet.forEach {
      if (!currentContactSet.contains(it)) {
        contactListener.endContact(it.fixture1, it.fixture2, fixedStep)
      }
    }
    priorContactSet = currentContactSet
    currentContactSet = OrderedSet()
  }

  private fun updatePhysics(body: Body, delta: Float) {
    body.physicsData.let {
      if (it.takeFrictionFromOthers) {
        if (it.frictionOnSelf.x > 0f) {
          it.velocity.x /= it.frictionOnSelf.x
        }
        if (it.frictionOnSelf.y > 0f) {
          it.velocity.y /= it.frictionOnSelf.y
        }
      }
      it.frictionOnSelf.set(it.defaultFrictionOnSelf)
      if (it.gravityOn) {
        it.velocity.add(it.gravity)
      }

      if (it.velocity.x > 0f && it.velocity.x > abs(it.velocityClamp.x)) {
        it.velocity.x = abs(it.velocityClamp.x)
      } else if (it.velocity.x < 0f && it.velocity.x < -abs(it.velocityClamp.x)) {
        it.velocity.x = -abs(it.velocityClamp.x)
      }

      body.x += it.velocity.x * delta
      body.y += it.velocity.y * delta
    }
    body.fixtures.forEach { f ->
      if (!f.attachedToBody) {
        return
      }
      f.shape.setCenter(body.getCenterPoint().add(f.offsetFromBodyCenter))
    }
  }

  private fun filterContact(fixture1: Fixture, fixture2: Fixture) =
      (fixture1 != fixture2) &&
          (contactFilterMap?.get(fixture1.fixtureType)?.contains(fixture2.fixtureType) == true ||
              contactFilterMap?.get(fixture2.fixtureType)?.contains(fixture1.fixtureType) == true)

  private fun resolveContacts(body: Body) {
    body.fixtures.forEach {
      if (it.active && contactFilterMap?.containsKey(it.fixtureType) == true) {
        val overlapping =
            worldGraph.data.getFixturesOverlapping(it) { o -> o.active && filterContact(it, o) }
        overlapping.forEach { o -> currentContactSet.add(Contact(it, o)) }
      }
    }

    val overlapping = worldGraph.data.getBodiesOverlapping(body)
    overlapping.forEach { collisionHandler.handleCollision(body, it) }
  }
}
