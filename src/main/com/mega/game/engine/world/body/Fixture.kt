package com.mega.game.engine.world.body

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.enums.Direction
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.common.shapes.ICardinallyRotatableShape2D
import com.mega.game.engine.common.shapes.IGameShape2D

/**
 * An implementation for [IFixture]. This implementation stores a shape variable labeled [rawShape] and
 * calculates the body-relative shape in [getShape].
 *
 * @param body The body this fixture belongs to. This is used when calculating the body-relative shape in [getShape].
 * @param rawShape The raw bounds of this fixture. Defaults to a [GameRectangle] with size of zero. To get the bounds
 * of this fixture relative to the body it is attached to, use [getShape].
 * @param fixtureType The type for this fixture. Used to determine how this fixture interacts with other
 *   fixtures. It can be anything (String, Int, Enum, etc.) so long as it properly implements
 *   [Any.equals] and [Any.hashCode] such that any two fixtures with the same type are considered
 *   equal (only in terms of contact interaction, not in terms of object equality) and any two
 *   fixtures with different types are considered not equal (again, only in terms of contact
 *   interaction).
 * @param active Whether this fixture is active. If not active, this fixture will not be used to
 *   detect collisions.
 * @param attachedToBody Whether this fixture is attached to a body. If not attached to a body, this
 *   fixture will not move with the body it is attached to.
 * @param offsetFromBodyCenter The offset of this fixture from the center of the body it is attached
 *   to. Used to position this fixture relative to the body it is attached to. This is the offset
 *   before the body is rotated.
 * @param properties The properties of this fixture.
 */
class Fixture(
    var body: Body,
    var fixtureType: Any,
    rawShape: IGameShape2D = GameRectangle(body),
    var active: Boolean = true,
    var attachedToBody: Boolean = true,
    var offsetFromBodyCenter: Vector2 = Vector2(),
    override var properties: Properties = Properties(),
) : IFixture, ICopyable<Fixture> {

    companion object {
        const val TAG = "Fixture"
    }

    /**
     * The raw shape used to generate this fixture's sensor. The raw shape is not used for contact detection, but rather
     * a copy of the raw shape. A new shape copy is created when the [getShape] method is called every time this
     * field is set.
     */
    var rawShape: IGameShape2D = rawShape
        set(value) {
            field = value
            shapeCopy = null
        }

    private var shapeCopy: IGameShape2D? = null
    private val reusableShapeProps = Properties()

    /**
     * Fetches the body-relative shape of this fixture. The body-relative shape is recalculated each time this function
     * is called. The body-relative shape is a copy of the [rawShape] that has been translated based on the body's center
     * and the value of [offsetFromBodyCenter]. The copy shape's origin is set to either the body's center or else the
     * body's origin based on the values of [Body.originXCenter] and [Body.originYCenter]. If [attachedToBody] is false,
     * then [rawShape] is returned unmodified.
     *
     * If [rawShape] is an instance of [ICardinallyRotatableShape2D] and the return value of [rawShape.copy()] is
     * also an instance of [ICardinallyRotatableShape2D], then the shape returned by this method is rotated based
     * on the [body]'s [Body.cardinalRotation] using [ICardinallyRotatableShape2D.getCardinallyRotatedShape]. If
     * the [body]'s [Body.cardinalRotation] value is null, then [Direction.UP] is used by default.
     *
     * @return the body-relative shape of this fixture
     */
    override fun getShape(): IGameShape2D {
        if (!attachedToBody) return rawShape

        reusableShapeProps.clear()
        rawShape.getProps(reusableShapeProps)

        if (shapeCopy == null) shapeCopy = rawShape.copy()
        shapeCopy!!.setWithProps(reusableShapeProps)

        val bodyCenter = body.getCenter()
        shapeCopy!!.setCenter(bodyCenter).translation(offsetFromBodyCenter)
        shapeCopy!!.originX = if (body.originXCenter) bodyCenter.x else body.originX
        shapeCopy!!.originY = if (body.originYCenter) bodyCenter.y else body.originY

        return if (shapeCopy !is ICardinallyRotatableShape2D) shapeCopy!!
        else (shapeCopy as ICardinallyRotatableShape2D).getCardinallyRotatedShape(body.cardinalRotation ?: Direction.UP)
    }

    override fun getType() = fixtureType

    override fun isActive() = active

    override fun toString() =
        "Fixture(raw_shape=$rawShape, type=$fixtureType, active=$active, attachedToBody=$attachedToBody, " +
                "offsetFromBodyCenter=$offsetFromBodyCenter, properties=$properties)"

    /**
     * Returns if this fixture overlaps the given shape.
     *
     * @param other the other shape
     * @return if this fixture overlaps the given shape
     */
    fun overlaps(other: IGameShape2D) = getShape().overlaps(other)

    /**
     * Returns if this fixture overlaps the given fixture.
     *
     * @param other the other fixture
     * @return if this fixture overlaps the given fixture
     */
    fun overlaps(other: IFixture) = overlaps(other.getShape())

    override fun copy() = Fixture(
        body,
        getType(),
        rawShape.copy() as ICardinallyRotatableShape2D,
        active,
        attachedToBody,
        offsetFromBodyCenter.cpy(),
        Properties(properties)
    )
}
