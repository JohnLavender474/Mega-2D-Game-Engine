package com.engine.world

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedSet
import com.engine.common.GameLogger
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
 * The [preProcess] function is run for each [Body] directly after the fixtureBody's own [Body.preProcess]
 * function has been run. This is useful if you want to run common logic for all bodies in the
 * pre-processing stage.
 *
 * The [postProcess] function is run for each [Body] directly after the fixtureBody's own
 * [Body.postProcess] function has been run. This is useful if you want to run common logic for all
 * bodies in the post-processing stage.
 *
 * @property contactListener the [IContactListener] to notify of contacts
 * @property worldGraphSupplier the supplier for the [IGraphMap] to use
 * @property fixedStep the fixed step to update the physics
 * @property collisionHandler the [ICollisionHandler] to resolve collisions
 * @property contactFilterMap the optional [ObjectMap] to filter contacts
 * @property debug whether to print debug statements
 */
class WorldSystem(
    private val contactListener: IContactListener,
    private val worldGraphSupplier: () -> IGraphMap?,
    private val fixedStep: Float,
    private val collisionHandler: ICollisionHandler = StandardCollisionHandler,
    private val contactFilterMap: ObjectMap<Any, ObjectSet<Any>>,
    var debug: Boolean = false
) : GameSystem(BodyComponent::class) {

    companion object {
        const val TAG = "WorldSystem"
        private const val MAX_DEBUG_TICKS = 60
    }

    internal var priorContactSet = OrderedSet<Contact>()
    internal var currentContactSet = OrderedSet<Contact>()
    internal var accumulator = 0f

    private val printDebugStatements: Boolean
        get() = debug && debugTicks == MAX_DEBUG_TICKS
    private var debugTicks = 0

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return
        accumulator += delta
        if (accumulator >= fixedStep) {
            val bodies = Array<Body>()
            entities.forEach {
                val body = it.getComponent(BodyComponent::class)!!.body
                bodies.add(body)
            }

            while (accumulator >= fixedStep) {
                accumulator -= fixedStep
                cycle(bodies, fixedStep)
            }

            val worldGraph = worldGraphSupplier()!!
            worldGraph.reset()
            bodies.forEach { body ->
                worldGraph.add(body, body.getBodyBounds())
                body.fixtures.forEach { (_, fixture) ->
                    worldGraph.add(fixture, fixture.getShape())
                }
            }
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
     * @param bodies the array of bodies to cycle through
     * @param delta the time in seconds since the last frame
     */
    internal fun cycle(bodies: Array<Body>, delta: Float) {
        if (debug) debugTicks++
        preProcess(bodies, delta)
        worldGraphSupplier()!!.reset()
        bodies.forEach { processPhysicsAndGraph(it, delta) }
        bodies.forEach { processContactsAndCollisions(it) }
        if (printDebugStatements) GameLogger.debug(TAG, "current contacts = $currentContactSet")
        processContacts()
        postProcess(bodies, delta)
        if (debugTicks >= MAX_DEBUG_TICKS) debugTicks = 0
    }

    /**
     * Pre-processes the bodies.
     *
     * @param bodies the array of bodies to run through the pre-process stage
     * @param delta the time in seconds since the last frame
     */
    internal fun preProcess(bodies: Array<Body>, delta: Float) {
        bodies.forEach { body -> body.preProcess.values().forEach { it.update(delta) } }
    }

    /**
     * Processes the physics and graph for the given fixtureBody.
     *
     * @param body the fixtureBody to process
     * @param delta the time in seconds since the last frame
     */
    internal fun processPhysicsAndGraph(body: Body, delta: Float) {
        val worldGraph = worldGraphSupplier() ?: throw IllegalStateException("World graph cannot be null.")
        updatePhysics(body, delta)
        worldGraph.add(body, body.getBodyBounds())
        body.fixtures.forEach { (_, fixture) ->
            worldGraph.add(fixture, fixture.getShape())
        }
    }

    /**
     * Processes the contacts and collisions for the given fixtureBody.
     *
     * @param body the fixtureBody to process
     */
    internal fun processContactsAndCollisions(body: Body) {
        checkForContacts(body)
        resolveCollisions(body)
    }

    /**
     * Post-processes the bodies.
     *
     * @param bodies the bodies to process
     * @param delta the time in seconds since the last frame
     */
    internal fun postProcess(bodies: Array<Body>, delta: Float) {
        bodies.forEach { body -> body.postProcess.values().forEach { it.update(delta) } }
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
     * Updates the physics of the given fixtureBody.
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
            if (it.velocity.x > 0f && it.velocity.x > abs(it.velocityClamp.x)) it.velocity.x =
                abs(it.velocityClamp.x)
            else if (it.velocity.x < 0f && it.velocity.x < -abs(it.velocityClamp.x)) it.velocity.x =
                -abs(it.velocityClamp.x)

            // clamp the y velocity
            if (it.velocity.y > 0f && it.velocity.y > abs(it.velocityClamp.y)) it.velocity.y =
                abs(it.velocityClamp.y)
            else if (it.velocity.y < 0f && it.velocity.y < -abs(it.velocityClamp.y)) it.velocity.y =
                -abs(it.velocityClamp.y)

            body.x += it.velocity.x * delta
            body.y += it.velocity.y * delta
        }
    }

    /**
     * Determines whether the given fixtures should be filtered. If there is a pairing between the fixture types of the
     * two fixtures in [contactFilterMap], then this method will return true. Otherwise, it will return false.
     *
     * @param fixture1 the first [Fixture] to filter
     * @param fixture2 the second [Fixture] to filter
     * @return whether the given fixtures should be filtered
     */
    internal fun filterContact(fixture1: IFixture, fixture2: IFixture) =
        (fixture1 != fixture2) && (contactFilterMap.get(fixture1.getFixtureType())?.contains(
            fixture2.getFixtureType()
        ) == true || contactFilterMap.get(fixture2.getFixtureType())?.contains(
            fixture1.getFixtureType()
        ) == true)

    /**
     * Checks for contacts with the given fixtureBody.
     *
     * @param body the [Body] to check for contacts
     */
    internal fun checkForContacts(body: Body) {
        val worldGraph = worldGraphSupplier() ?: throw IllegalStateException("World graph cannot be null.")

        body.fixtures.forEach { (_, fixture) ->
            if (fixture.isActive() && contactFilterMap?.containsKey(fixture.getFixtureType()) != false) {
                if (printDebugStatements) GameLogger.debug(TAG, "checking for contacts with fixture = $fixture")

                val overlapping = ObjectSet<IFixture>()

                val worldGraphResults = worldGraph.get(fixture.getShape())
                if (printDebugStatements) GameLogger.debug(TAG, "world graph results = $worldGraphResults")

                worldGraphResults.forEach {
                    if (it is IFixture && it.isActive() && filterContact(fixture, it) &&
                        fixture.getShape().overlaps(it.getShape())
                    ) overlapping.add(it)
                }

                if (printDebugStatements) GameLogger.debug(TAG, "overlapping fixtures = $overlapping")

                overlapping.forEach { o -> currentContactSet.add(Contact(fixture, o)) }
            }
        }
    }

    /**
     * Resolves the collisions of the given fixtureBody. This method is responsible for resolving the collisions of the given
     * fixtureBody and notifying the [ICollisionHandler] of any collisions that occur.
     *
     * @param body the [Body] to resolve the collisions of
     */
    internal fun resolveCollisions(body: Body) {
        val worldGraph = worldGraphSupplier()!!
        worldGraph.get(body.getBodyBounds()).forEach {
            if (it is Body && it != body && it.getBodyBounds().overlaps(body as Rectangle))
                collisionHandler.handleCollision(body, it)
        }
    }
}
