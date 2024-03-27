package com.engine.world

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.GameLogger
import com.engine.common.enums.Direction
import com.engine.common.interfaces.IPropertizable
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle

/**
 * A [Body] is a [GameRectangle] representing the fixtureBody of an object in the game world. It contains a
 * [BodyType] which determines how it interacts with other bodies. It also contains a [PhysicsData]
 * object which contains information about the fixtureBody's physical properties. It also contains a list
 * of [Fixture]s which are used to determine collisions. It also contains a [HashMap] of user data
 * which can be used to store any data associated with the fixtureBody. It also contains a [preProcess] and
 * [postProcess] [Updatable] which are used to perform any actions before or after the fixtureBody is
 * processed by the [WorldSystem].
 *
 * @param bodyType the [BodyType] of the fixtureBody
 * @param x the first position of the fixtureBody
 * @param y the second position of the fixtureBody
 * @param width the width of the fixtureBody
 * @param height the height of the fixtureBody
 * @param physics the [PhysicsData] of the fixtureBody
 * @param fixtures the [OrderedMap] of [Fixture]s of the fixtureBody
 * @param properties the [Properties] of the fixtureBody
 * @param preProcess the [Updatable] to run before the fixtureBody is processed
 * @param postProcess the [Updatable] to run after the fixtureBody is processed
 * @param cardinalRotation the [Direction] of the fixtureBody; this determines the rotation of the fixtureBody
 *   returned by [rotatedBounds]. If the [cardinalRotation] is [Direction.UP], then the rotation is
 *   0f which means essentially that no rotation is performed. This field is used in determining
 *   fixtureBody collisions in [WorldSystem] and [StandardCollisionHandler]. The default value is
 *   [Direction.UP]. If you do not wish to use the built-in rotation logic, then it is best to keep
 *   this field the default value.
 * @param originXCenter if true, the center x of the fixtureBody will be used as the origin x instead of
 *   the value of [originX] when calculating [rotatedBounds]. The origin only affects the value of
 *   [rotatedBounds]. If your intention is to merely rotate the fixtureBody relative to itself (i.e. rotate
 *   but not move), then you should set this to true. The default value is true. If you do not wish
 *   to use the built-in rotation logic, then it is best to keep this field the default value.
 * @param originYCenter if true, the center y of the fixtureBody will be used as the origin y instead of
 *   the value of [originY] when calculating [rotatedBounds]. The origin only affects the value of
 *   [rotatedBounds]. If your intention is to merely rotate the fixtureBody relative to itself (i.e. rotate
 *   but not move), then you should set this to true. The default value is true. If you do not wish
 *   to use the built-in rotation logic, then it is best to keep this field the default value.
 * @see GameRectangle
 * @see BodyType
 * @see PhysicsData
 * @see Fixture
 * @see Updatable
 * @see WorldSystem
 * @see Resettable
 */
open class Body(
    var bodyType: BodyType,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f,
    var physics: PhysicsData = PhysicsData(),
    var fixtures: Array<Pair<Any, IFixture>> = Array(),
    override var properties: Properties = Properties(),
    var preProcess: OrderedMap<Any, Updatable> = OrderedMap(),
    var postProcess: OrderedMap<Any, Updatable> = OrderedMap(),
    var cardinalRotation: Direction = Direction.UP,
    var originXCenter: Boolean = true,
    var originYCenter: Boolean = true
) : GameRectangle(x, y, width, height), Resettable, IPropertizable {

    companion object {
        const val TAG = "Body"
    }

    /**
     * Creates a [Body] with the given [BodyType], [PhysicsData], [ArrayList] of [Fixture]s, and
     * [HashMap] of user data.
     *
     * @param bodyType the [BodyType] of the fixtureBody
     * @param physicsData the [PhysicsData] of the fixtureBody
     * @param fixtures the [OrderedMap] of [Fixture]s of the fixtureBody
     * @param properties the [Properties] of the fixtureBody
     * @param preProcess the [Updatable] to run before the fixtureBody is processed
     * @param postProcess the [Updatable] to run after the fixtureBody is processed
     */
    constructor(
        bodyType: BodyType,
        physicsData: PhysicsData,
        fixtures: Array<Pair<Any, IFixture>> = Array(),
        properties: Properties = Properties(),
        preProcess: OrderedMap<Any, Updatable> = OrderedMap(),
        postProcess: OrderedMap<Any, Updatable> = OrderedMap()
    ) : this(bodyType, 0f, 0f, 0f, 0f, physicsData, fixtures, properties, preProcess, postProcess)

    /**
     * Returns the bounds of this fixtureBody rotated in the given [Direction]. If the [cardinalRotation] is
     * [Direction.UP], then the rotation is 0f which means essentially that no rotation is performed.
     * If [originXCenter] is true, then the center x of the fixtureBody will be used as the origin x instead
     * of the value of [originX]. If [originYCenter] is true, then the center y of the fixtureBody will be
     * used as the origin y instead of the value of [originY]. The origin only affects the value of
     * [rotatedBounds]. The default value of [cardinalRotation] is [Direction.UP]. The default value
     * of [originXCenter] is true. The default value of [originYCenter] is true.
     */
    open val rotatedBounds: GameRectangle
        get() {
            val center = getCenter()
            val copy = GameRectangle(this)
            copy.originX = if (originXCenter) center.x else originX
            copy.originY = if (originYCenter) center.y else originY
            return copy.getCardinallyRotatedShape(cardinalRotation, false)
        }

    /**
     * Returns if the fixtureBody has the given [BodyType].
     *
     * @param bodyType the [BodyType] to check
     * @return if the fixtureBody is the given [BodyType]
     */
    fun isBodyType(bodyType: BodyType) = this.bodyType == bodyType

    /**
     * Adds the given [Fixture] to this fixtureBody. The fixture is mapped to the fixture's type.
     *
     * @param fixture the [Fixture] to add
     */
    open fun addFixture(fixture: IFixture): Body {
        fixtures.add(fixture.getFixtureType() to fixture)
        return this
    }

    /**
     * Runs the given function for each fixture entry in this fixtureBody.
     *
     * @param function the function to run for each fixture entry
     */
    fun forEachFixture(function: (Any, IFixture) -> Unit) =
        fixtures.forEach { function(it.first, it.second) }

    /**
     * Resets the fixtureBody to its default state by resetting its [PhysicsData] and resetting the positions
     * of its [Fixture]s to their default positions (offset from the center of the fixtureBody).
     *
     * @see Resettable.reset
     */
    override fun reset() {
        physics.reset()
    }

    override fun copy(): Body {
        GameLogger.debug(TAG, "copy(): Creating copy of fixtureBody: $this.")
        val body =
            Body(
                bodyType,
                x,
                y,
                width,
                height,
                physics.copy(),
                Array(),
                properties.copy(),
                preProcess,
                postProcess
            )
        fixtures.forEach { body.addFixture(it.second.copy()) }
        return body
    }

    override fun equals(other: Any?) = this === other

    override fun hashCode() = System.identityHashCode(this)
}
