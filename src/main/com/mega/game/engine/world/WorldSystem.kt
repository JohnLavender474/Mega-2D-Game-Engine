package com.mega.game.engine.world

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedSet
import com.mega.game.engine.common.extensions.exp
import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.common.objects.Pool
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem
import com.mega.game.engine.world.body.Body
import com.mega.game.engine.world.body.BodyComponent
import com.mega.game.engine.world.body.IFixture
import com.mega.game.engine.world.body.PhysicsData
import com.mega.game.engine.world.collisions.ICollisionHandler
import com.mega.game.engine.world.contacts.Contact
import com.mega.game.engine.world.contacts.IContactListener
import com.mega.game.engine.world.container.IWorldContainer
import kotlin.math.abs

/**
 * A system that handles the physics of the game. This system is responsible for updating the
 * positions of all bodies, resolving collisions, and notifying the [IContactListener] of any
 * contacts that occur. This system is stateful. This system processes entities that have a
 * [BodyComponent].
 *
 * This system uses a [IWorldContainer] to represent the world and determine which bodies are overlapping. This might be
 * a representation of the game level where the x and y of the graph map is the min coordinate of the level and the max
 * x and max y are determined by the min coordinate plus the width or height of the level. Although the lambda allows
 * for the container object to be nullable, if it is null when [WorldSystem] is run, then a null-pointer exception will
 * be thrown.
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
 * For each "step" of the world system, the [updatePhysics] method is called on each body. This method is
 * open if the user would like to change the implementation of this method.
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
 * The [fixedStepScalar] property is useful for speeding up or slowing down the delta time, mainly for debugging
 * purposes. It should be set to 1f (the default value) in production. The value must be greater than 0f or else an
 * exception will be thrown.
 *
 * @property ppm the pixels per meter of the world representation
 * @property contactListener the [IContactListener] to notify of contacts
 * @property worldContainerSupplier the supplier for the [IWorldContainer] to use
 * @property fixedStep the fixed step to update the physics
 * @property collisionHandler the [ICollisionHandler] to resolve collisions
 * @property contactFilterMap the optional [ObjectMap] to filter contacts
 * @property fixedStepScalar the scalar to apply to the fixed step, useful for debugging; should be 1f in prod.
 * Default value is 1f (no scaling applied). If the value is less than 1f, the rendering of physics will be slowed down.
 * If greater than 1f, then rendering will be sped up. Must be greater than 0f or else an exception will be thrown.
 */
open class WorldSystem(
    private val ppm: Int,
    private val fixedStep: Float,
    private val worldContainerSupplier: () -> IWorldContainer?,
    private val contactListener: IContactListener,
    private val collisionHandler: ICollisionHandler,
    private val contactFilterMap: ObjectMap<Any, ObjectSet<Any>>,
    var fixedStepScalar: Float = 1f
) : GameSystem(BodyComponent::class) {

    companion object {
        const val TAG = "WorldSystem"
    }

    init {
        if (fixedStepScalar <= 0f) throw IllegalArgumentException("Value of fixedStepScalar must be greater than 0")
    }

    private class DummyFixture : IFixture {
        override fun getShape() =
            throw IllegalStateException("The `getType` method should never be called on a DummyFixture instance")

        override fun isActive() =
            throw IllegalStateException("The `getType` method should never be called on a DummyFixture instance")

        override fun getType() =
            throw IllegalStateException("The `getType` method should never be called on a DummyFixture instance")

        override val properties: Properties
            get() = throw IllegalStateException("The `getType` method should never be called on a DummyFixture instance")
    }

    private val worldContainer: IWorldContainer
        get() = worldContainerSupplier()!!
    private val reusableBodyArray = Array<Body>()
    private val contactPool = Pool(supplier = { Contact(DummyFixture(), DummyFixture()) })
    private val reusableGameRect = GameRectangle()

    private var priorContactSet = OrderedSet<Contact>()
    private var currentContactSet = OrderedSet<Contact>()
    private var accumulator = 0f

    /**
     * If [on] is false, then nothing occurs.
     *
     * Accumulates the delta time. While the accumulated delta time is greater than [fixedStep], the fixed step value
     * (divided by the [fixedStepScalar] value) is subtracted from the accumulated delta time, and the world system's
     * "cycle" is performed.
     *
     * At the end of each cycle, the [worldContainer] is cleared and repopulated with all bodies and fixtures contained
     * in the world system.
     *
     */
    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        accumulator += delta
        if (accumulator >= fixedStep) {
            entities.forEach {
                val body = it.getComponent(BodyComponent::class)!!.body
                reusableBodyArray.add(body)
            }

            while (accumulator >= fixedStep) {
                accumulator -= fixedStep / fixedStepScalar
                cycle(reusableBodyArray, fixedStep)
            }

            worldContainer.clear()
            reusableBodyArray.forEach { body ->
                worldContainer.addBody(body)
                body.fixtures.forEach { (_, fixture) -> worldContainer.addFixture(fixture) }
            }

            reusableBodyArray.clear()
        }
    }

    /**
     * Performs the following operations:
     * - Resets the delta-time accumulator back to zero
     * - Clears all contacts
     * - Clears the [worldContainer]
     */
    override fun reset() {
        super.reset()
        accumulator = 0f
        priorContactSet.forEach { contactPool.pool(it) }
        currentContactSet.forEach { contactPool.pool(it) }
        priorContactSet.clear()
        currentContactSet.clear()
        worldContainerSupplier()?.clear()
    }

    /**
     * Returns true if the two fixtures can make contact.
     *
     * The default implementation of this method returns true if the following conditions are met:
     * - The two fixtures are not the same
     * - The [contactFilterMap] contains a key-value pairing for the two fixtures (where the map contains either
     *      fixture's type as a key and the key's value set contains the other fixture's key)
     */
    open fun filterContact(fixture1: IFixture, fixture2: IFixture) =
        (fixture1 != fixture2) &&
                (contactFilterMap.get(fixture1.getType())?.contains(fixture2.getType()) == true ||
                        contactFilterMap.get(fixture2.getType())?.contains(fixture1.getType()) == true)

    private fun cycle(bodies: Array<Body>, delta: Float) {
        preProcess(bodies, delta)

        worldContainer.clear()
        bodies.forEach { body ->
            updatePhysics(body, delta)
            worldContainer.addBody(body)
            body.fixtures.forEach { (_, fixture) -> worldContainer.addFixture(fixture) }
        }

        bodies.forEach { body -> collectContacts(body, currentContactSet) }
        processContacts()

        bodies.forEach { body -> resolveCollisions(body) }

        postProcess(bodies, delta)
    }

    private fun preProcess(bodies: Array<Body>, delta: Float) =
        bodies.forEach { body -> body.preProcess.values().forEach { it.update(delta) } }

    private fun postProcess(bodies: Array<Body>, delta: Float) =
        bodies.forEach { body -> body.postProcess.values().forEach { it.update(delta) } }

    private fun processContacts() {
        currentContactSet.forEach {
            if (priorContactSet.contains(it)) contactListener.continueContact(it, fixedStep)
            else contactListener.beginContact(it, fixedStep)
        }

        priorContactSet.forEach {
            if (!currentContactSet.contains(it)) {
                contactListener.endContact(it, fixedStep)
                contactPool.pool(it)
            }
        }

        priorContactSet.clear()
        priorContactSet.addAll(currentContactSet)
        currentContactSet.clear()
    }

    /**
     * Updates the physics of a body based on its current velocity, friction, and gravity, over a given time interval.
     *
     * The default implementation of this method follows the following steps:
     *
     * 1. If [PhysicsData.applyFrictionX] is true for the body's physics data instance, then the x value of
     *    [PhysicsData.frictionOnSelf] is applied to the body's velocity in the x direction. This is done by multiplying
     *    the current x velocity by the exponential decay factor based on the friction value and the time step [delta].
     *    See the [exp] function.
     *
     * 2. If [PhysicsData.applyFrictionY] is true for the body's physics data instance, then the y value of
     *    [PhysicsData.frictionOnSelf] is applied to the body's velocity in the y direction, using a similar
     *    exponential decay as in the x direction.
     *
     * 3. After applying friction, the friction values for both x and y directions are reset to their default values
     *    by calling [PhysicsData.defaultFrictionOnSelf].
     *
     * 4. If [PhysicsData.gravityOn] is true, the current gravity is applied to the body's velocity. This adds
     *    the gravity vector to the velocity vector, affecting both x and y directions as defined by the gravity value.
     *
     * 5. The body's velocity is clamped to ensure that it stays within the limits defined by [PhysicsData.velocityClamp].
     *    Both the x and y components of the velocity are constrained to their respective maximum values, ensuring the
     *    body does not move faster than allowed.
     *
     * 6. Finally, the body's position is updated based on the current velocity and the time step [delta]. The new
     *    x and y positions are calculated by adding the product of velocity and delta to the body's current position.
     *
     * @param body The [Body] whose physics should be updated.
     * @param delta The time step over which to apply the physics updates. This is typically the frame time or
     *              delta time in a game loop.
     */
    open fun updatePhysics(body: Body, delta: Float) {
        body.physics.let { physics ->
            if (physics.applyFrictionX && physics.frictionOnSelf.x > 0f)
                physics.velocity.x *= exp(-physics.frictionOnSelf.x * delta)
            if (physics.applyFrictionY && physics.frictionOnSelf.y > 0f)
                physics.velocity.y *= exp(-physics.frictionOnSelf.y * delta)
            physics.frictionOnSelf.set(physics.defaultFrictionOnSelf)

            if (physics.gravityOn) physics.velocity.add(physics.gravity)

            physics.velocity.x =
                physics.velocity.x.coerceIn(-abs(physics.velocityClamp.x), abs(physics.velocityClamp.x))
            physics.velocity.y =
                physics.velocity.y.coerceIn(-abs(physics.velocityClamp.y), abs(physics.velocityClamp.y))

            body.x += physics.velocity.x * delta
            body.y += physics.velocity.y * delta
        }
    }

    /**
     * The default implementation of this method collects all the contacts for a given body by iterating over its active
     * fixtures and checking for potential overlaps with other fixtures in the world. Contacts are added to the provided
     * [contactSet] if they meet the specified filtering conditions and overlap with the given body's fixtures.
     *
     * The default implementation follows these steps:
     * 1. Iterates over all fixtures of the given [body].
     * 2. For each active fixture, checks if the fixture type has an associated contact filter in [contactFilterMap].
     * 3. If a contact filter is found, the fixture's bounding rectangle is calculated using its shape.
     * 4. The method then queries the [worldContainer] to retrieve all fixtures within the area defined by the fixture's
     *    bounding rectangle.
     * 5. For each retrieved fixture, the method:
     *    - Checks if it is active.
     *    - Applies the [filterContact] method to see if the fixture can be considered for contact.
     *    - Checks if the fixture's shape overlaps with the current fixture.
     * 6. If all conditions are met, a [Contact] object is created and added to the [contactSet].
     *
     * @param body The [Body] whose contacts are being collected.
     * @param contactSet The set where valid contacts will be added.
     */
    open fun collectContacts(body: Body, contactSet: ObjectSet<Contact>) = body.fixtures.forEach { (_, fixture) ->
        if (fixture.isActive() && contactFilterMap.containsKey(fixture.getType())) {
            fixture.getShape().getBoundingRectangle(reusableGameRect)
            val worldGraphResults = worldContainer.getFixtures(
                MathUtils.floor(reusableGameRect.x / ppm),
                MathUtils.floor(reusableGameRect.y / ppm),
                MathUtils.floor(reusableGameRect.getMaxX() / ppm),
                MathUtils.floor(reusableGameRect.getMaxY() / ppm)
            )

            worldGraphResults.forEach {
                if (it.isActive() && filterContact(fixture, it) &&
                    fixture.getShape().overlaps(it.getShape())
                ) {
                    val contact = contactPool.fetch()
                    contact.set(fixture, it)
                    contactSet.add(contact)
                }
            }
        }
    }

    /**
     * Resolves this body's collisions.
     *
     * The default implementation performs the following steps:
     * - Retrieves the body's adjusted bounds via the [Body.getBodyBounds] method
     * - Retrieves all bodies overlapping (or close to overlapping) the adjusted bounds via the [worldContainer]'s
     * [IWorldContainer.getBodies] method where
     *   - minX = adjusted bound's x divided by [ppm]
     *   - minY = adjusted bound's y divided by [ppm]
     *   - maxX = adjusted bound's max x divided by [ppm]
     *   - maxY = adjusted bound's max y divided by [ppm]
     * - For each retrieved body, if it does not equal the body and its adjusted bounds overlaps the body's adjusted
     *      bounds, then both bodies are passed into the [collisionHandler]'s [ICollisionHandler.handleCollision] method.
     */
    open fun resolveCollisions(body: Body) {
        val bounds = body.getBodyBounds()
        worldContainer.getBodies(
            MathUtils.floor(bounds.x / ppm),
            MathUtils.floor(bounds.y / ppm),
            MathUtils.floor(bounds.getMaxX() / ppm),
            MathUtils.floor(bounds.getMaxY() / ppm)
        ).forEach {
            if (it != body && it.getBodyBounds().overlaps(bounds as Rectangle))
                collisionHandler.handleCollision(body, it)
        }
    }
}
