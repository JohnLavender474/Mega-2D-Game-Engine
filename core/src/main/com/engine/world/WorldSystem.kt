package com.engine.world

import com.badlogic.gdx.utils.OrderedSet
import com.engine.Entity
import com.engine.GameSystem
import com.engine.common.objects.DataWrapper
import kotlin.math.abs

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
      1 -> resolveContacts(body, delta)
    }
  }

  private fun postProcess(entities: ArrayList<Entity>, delta: Float) =
      entities.forEach { e ->
        val body = e.getComponent(BodyComponent::class).body
        worldGraph.data.addBody(body)
        body.fixtures.forEach { f -> worldGraph.data.addFixture(f) }
        body.postProcess?.update(delta)
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

  private fun resolveContacts(body: Body, delta: Float) {
    body.fixtures.forEach {
      if (it.active && contactFilterMap?.containsKey(it.fixtureType) == true) {
        val overlapping =
            worldGraph.data.getFixturesOverlapping(it) { o -> o.active && filterContact(it, o) }
        overlapping.forEach { o -> currentContactSet.add(Contact(it, o)) }
      }
    }

    val overlapping = worldGraph.data.getBodiesOverlapping(body)
    overlapping.forEach { collisionHandler.handleCollision(body, it, delta) }
  }
}
