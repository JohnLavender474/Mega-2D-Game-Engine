package com.mega.game.engine.world.body

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.enums.Direction
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.common.shapes.GameRectangle

/**
 * A [Body] is a [GameRectangle] representing the body of an object in the game world. It contains a
 * [BodyType] which determines how it interacts with other bodies. It also contains a [PhysicsData]
 * object which contains information about the body's physical properties. It also contains a list
 * of [Fixture]s which are used to determine collisions. It also contains a [HashMap] of user data
 * which can be used to store any data associated with the body. It also contains a [preProcess] and
 * [postProcess] [Updatable] which are used to perform any actions before or after the body is
 * processed.
 *
 * @param bodyType the [BodyType] of the body
 * @param x the first position of the body
 * @param y the second position of the body
 * @param width the width of the body
 * @param height the height of the body
 * @param physics the [PhysicsData] of the body
 * @param fixtures the [OrderedMap] of [Fixture]s of the body
 * @param properties the [Properties] of the body
 * @param preProcess the [Updatable] to run before the body is processed
 * @param postProcess the [Updatable] to run after the body is processed
 * @param cardinalRotation the [Direction] of the body; this determines the rotation of the body
 *   returned by [getBodyBounds()]. If the [cardinalRotation] is [Direction.UP], then the rotation is
 *   0f which means essentially that no rotation is performed. The default value is [Direction.UP].
 *   If you do not wish to use the built-in rotation logic, then it is best to keep this field the
 *   default value.
 * @param originXCenter if true, the center x of the body will be used as the origin x instead of
 *   the value of [originX] when calculating [getBodyBounds()]. The origin only affects the value of
 *   [getBodyBounds()]. If your intention is to merely rotate the body relative to itself (i.e. rotate
 *   but not move), then you should set this to true. The default value is true. If you do not wish
 *   to use the built-in rotation logic, then it is best to keep this field the default value.
 * @param originYCenter if true, the center y of the body will be used as the origin y instead of
 *   the value of [originY] when calculating [getBodyBounds()]. The origin only affects the value of
 *   [getBodyBounds()]. If your intention is to merely rotate the body relative to itself (i.e. rotate
 *   but not move), then you should set this to true. The default value is true. If you do not wish
 *   to use the built-in rotation logic, then it is best to keep this field the default value.
 * @param onReset Optional lambda to call in the [reset] method.
 * @see GameRectangle
 * @see BodyType
 * @see PhysicsData
 * @see Fixture
 * @see Updatable
 * @see Resettable
 */
class Body(
    var bodyType: BodyType,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f,
    var physics: PhysicsData = PhysicsData(),
    var fixtures: Array<GamePair<Any, IFixture>> = Array(),
    var preProcess: OrderedMap<Any, Updatable> = OrderedMap(),
    var postProcess: OrderedMap<Any, Updatable> = OrderedMap(),
    var cardinalRotation: Direction = Direction.UP,
    var originXCenter: Boolean = true,
    var originYCenter: Boolean = true,
    var onReset: (() -> Unit)? = null,
    override var properties: Properties = Properties(),
) : GameRectangle(x, y, width, height), Resettable, IPropertizable {

    companion object {
        const val TAG = "Body"
    }

    /**
     * Creates a [Body] with the given [BodyType], [PhysicsData], [ArrayList] of [Fixture]s, and
     * [HashMap] of user data.
     *
     * @param bodyType the [BodyType] of the body
     * @param physicsData the [PhysicsData] of the body
     * @param fixtures the [OrderedMap] of [Fixture]s of the body
     * @param properties the [Properties] of the body
     * @param preProcess the [Updatable] to run before the body is processed
     * @param postProcess the [Updatable] to run after the body is processed
     */
    constructor(
        bodyType: BodyType,
        physicsData: PhysicsData,
        fixtures: Array<GamePair<Any, IFixture>> = Array(),
        properties: Properties = Properties(),
        preProcess: OrderedMap<Any, Updatable> = OrderedMap(),
        postProcess: OrderedMap<Any, Updatable> = OrderedMap()
    ) : this(bodyType, 0f, 0f, 0f, 0f, physicsData, fixtures, preProcess, postProcess, properties = properties)

    private val copyBounds = GameRectangle()

    /**
     * Returns the bounds of this body. If [cardinalRotation] is [Direction.UP], then this method simply returns "this"
     * since the rotation is zero. For other direction values, a copy of the body's bounds is rotated to the value of
     * [Direction.rotation] of [cardinalRotation] and is returned. If [originXCenter] is true, then the center x of
     * the body will be used as the origin x instead of the value of [originX]. If [originYCenter] is true, then the
     * center y of the body will be used as the origin y instead of the value of [originY]. The origin only affects the
     * return value of this method. The default value of [cardinalRotation] is null. The default value of
     * [originXCenter] is true. The default value of [originYCenter] is true.
     *
     * @return the bounds of this body
     */
    fun getBodyBounds(): GameRectangle {
        copyBounds.set(this)
        val center = getCenter()
        copyBounds.originX = if (originXCenter) center.x else originX
        copyBounds.originY = if (originYCenter) center.y else originY
        copyBounds.getCardinallyRotatedShape(cardinalRotation)
        return copyBounds
    }

    /**
     * Returns if the body has the given [BodyType].
     *
     * @param bodyType the [BodyType] to check
     * @return if the body is the given [BodyType]
     */
    fun isBodyType(bodyType: BodyType) = this.bodyType == bodyType

    /**
     * Adds the given [Fixture] to this body. The fixture is mapped to the fixture's type.
     *
     * @param fixture the [Fixture] to add
     */
    fun addFixture(fixture: IFixture): Body {
        fixtures.add(fixture.getType() pairTo fixture)
        return this
    }

    /**
     * Resets the body to its default state by resetting its [PhysicsData] and resetting the positions
     * of its [Fixture]s to their default positions (offset from the center of the body).
     *
     * @see Resettable.reset
     */
    override fun reset() {
        physics.reset()
        onReset?.invoke()
    }

    override fun equals(other: Any?) = this === other

    override fun hashCode() = System.identityHashCode(this)
}
