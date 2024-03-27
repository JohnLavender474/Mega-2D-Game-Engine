package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.objects.Properties
import com.engine.common.shapes.IGameShape2D

/**
 * An implementation for [IFixture]. This implementation stores a shape variable labeled [rawShape] and
 * calculates the fixtureBody-relative shape in [getShape].
 *
 * @param rawShape The raw bounds of this fixture. To get the bounds of this fixture relative to the
 *   fixtureBody it is attached to, use [getShape].
 * @param type The type for this fixture. Used to determine how this fixture interacts with other
 *   fixtures. It can be anything (String, Int, Enum, etc.) so long as it properly implements
 *   [Any.equals] and [Any.hashCode] such that any two fixtures with the same type are considered
 *   equal (only in terms of contact interaction, not in terms of object equality) and any two
 *   fixtures with different types are considered not equal (again, only in terms of contact
 *   interaction).
 * @param active Whether this fixture is active. If not active, this fixture will not be used to
 *   detect collisions.
 * @param attachedToBody Whether this fixture is attached to a fixtureBody. If not attached to a fixtureBody, this
 *   fixture will not move with the fixtureBody it is attached to.
 * @param offsetFromBodyCenter The offset of this fixture from the center of the fixtureBody it is attached
 *   to. Used to position this fixture relative to the fixtureBody it is attached to. This is the offset
 *   before the fixtureBody is rotated.
 * @param properties The properties of this fixture.
 */
open class Fixture(
    open var fixtureBody: Body,
    open var type: Any,
    open var rawShape: IGameShape2D,
    open var active: Boolean = true,
    open var attachedToBody: Boolean = true,
    open var offsetFromBodyCenter: Vector2 = Vector2(),
    override var properties: Properties = Properties(),
) : IFixture {

    companion object {
        const val TAG = "Fixture"
    }

    override fun getBody() = fixtureBody

    /**
     * Fetches a copy of the body-relative shape of this fixture. The body-relative shape is recalculated each
     * time this function is called. If [attachedToBody] is false, then [rawShape] is returned unmodified.
     *
     * @return the body-relative shape of this fixture
     */
    override fun getShape(): IGameShape2D {
        if (!attachedToBody) return rawShape

        val bodyCenter = fixtureBody.rotatedBounds.getCenter()
        rawShape.setCenter(bodyCenter).translation(offsetFromBodyCenter)

        val relativeShape = rawShape.copy()
        relativeShape.originX = if (fixtureBody.originXCenter) bodyCenter.x else fixtureBody.originX
        relativeShape.originY = if (fixtureBody.originYCenter) bodyCenter.y else fixtureBody.originY

        val cardinalRotation = fixtureBody.cardinalRotation
        val bodyRelativeShape = relativeShape.getCardinallyRotatedShape(cardinalRotation, false)

        bodyRelativeShape.color = rawShape.color
        bodyRelativeShape.shapeType = rawShape.shapeType

        return bodyRelativeShape
    }

    override fun getFixtureType() = type

    override fun isActive() = active

    override fun copy() = Fixture(
        fixtureBody, type, rawShape.copy(), active, attachedToBody, Vector2(offsetFromBodyCenter), properties.copy()
    )

    override fun toString() =
        "Fixture(raw_shape=$rawShape, type=$type, active=$active, attachedToBody=$attachedToBody, " +
                "offsetFromBodyCenter=$offsetFromBodyCenter, properties=$properties)"

    /**
     * Returns if this fixture overlaps the given shape.
     *
     * @param other the other shape
     * @return if this fixture overlaps the given shape
     */
    open fun overlaps(other: IGameShape2D) = getShape().overlaps(other)

    /**
     * Returns if this fixture overlaps the given fixture.
     *
     * @param other the other fixture
     * @return if this fixture overlaps the given fixture
     */
    open fun overlaps(other: IFixture) = overlaps(other.getShape())
}
