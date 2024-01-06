package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.GameLogger
import com.engine.common.interfaces.ICopyable
import com.engine.common.interfaces.IPropertizable
import com.engine.common.objects.Properties
import com.engine.common.shapes.IGameShape2D

/**
 * A fixture for a [Body]. A fixture is a shape that is attached to a body. It can be used to detect
 * collisions with other fixtures.
 *
 * @param shape The raw bounds of this fixture. To get the bounds of this fixture relative to the
 *   body it is attached to, use [bodyRelativeShape] which is set in each update cycle in
 *   [WorldSystem].
 * @param fixtureLabel The label for this fixture. Used to determine how this fixture interacts with
 *   other fixtures. It can be anything (String, Int, Enum, etc.) so long as it properly implements
 *   [Any.equals] and [Any.hashCode] such that any two fixtures with the same label are considered
 *   equal (in terms of labeling and contact interaction) and any two fixtures with different labels
 *   are considered not equal (again in terms of labeling and contact interaction).
 * @param active Whether this fixture is active. If not active, this fixture will not be used to
 *   detect collisions.
 * @param attachedToBody Whether this fixture is attached to a body. If not attached to a body, this
 *   fixture will not move with the body it is attached to.
 * @param offsetFromBodyCenter The offset of this fixture from the center of the body it is attached
 *   to. Used to position this fixture relative to the body it is attached to.
 * @param properties The properties of this fixture.
 */
class Fixture(
    var shape: IGameShape2D,
    var fixtureLabel: Any,
    var active: Boolean = true,
    var attachedToBody: Boolean = true,
    var offsetFromBodyCenter: Vector2 = Vector2(),
    override var properties: Properties = Properties(),
) : IPropertizable, ICopyable {

  companion object {
    const val TAG = "Fixture"
  }

  /**
   * The shape of this fixture relative to the body it is attached to. This is null until
   * [setBodyRelativeShape] is called.
   */
  var bodyRelativeShape: IGameShape2D? = null
    private set

  /**
   * Sets the bounds of this fixture relative to the body it is attached to. This is calculated by
   * taking the bounds of this fixture and translating it by the offset of this fixture from the
   * center of the body it is attached to.
   *
   * @param body The body to set the bounds relative to.
   */
  fun setBodyRelativeShape(body: Body) {
    val bodyCenter = body.getCenter()
    shape.setCenter(bodyCenter).translation(offsetFromBodyCenter)

    val relativeShape = shape.copy() as IGameShape2D
    relativeShape.originX = if (body.originXCenter) bodyCenter.x else body.originX
    relativeShape.originY = if (body.originYCenter) bodyCenter.y else body.originY
    val cardinalRotation = body.cardinalRotation
    bodyRelativeShape = relativeShape.getCardinallyRotatedShape(cardinalRotation, false)
    bodyRelativeShape!!.color = shape.color
    bodyRelativeShape!!.shapeType = shape.shapeType

    GameLogger.debug(
        TAG,
        "setBodyRelativeShape(): cardinal rotation = $cardinalRotation, raw shape = $shape, relative shape = $bodyRelativeShape.")
  }

  /**
   * Returns if this fixture's [bodyRelativeShape] overlaps the given [bodyRelativeShape]. If the
   * [bodyRelativeShape] has not been set yet via [setBodyRelativeShape], then false is returned.
   *
   * @return Whether this fixture's [bodyRelativeShape] overlaps the given [bodyRelativeShape], or
   *   false if [bodyRelativeShape] is null.
   * @see IGameShape2D.overlaps
   */
  fun overlaps(other: IGameShape2D) = bodyRelativeShape?.overlaps(other) ?: false

  /**
   * Returns if this fixture overlaps the given fixture. Specifically, this method returns if this
   * fixture's [bodyRelativeShape] overlaps the given fixture's [bodyRelativeShape]. Throws a
   * [NullPointerException] if [bodyRelativeShape] of the provided fixture is null.
   *
   * @param other The fixture to check if this fixture overlaps.
   * @throws NullPointerException If [bodyRelativeShape] of the provided fixture is null.
   * @see overlaps
   */
  fun overlaps(other: Fixture) = overlaps(other.bodyRelativeShape!!)

  override fun copy(): Fixture {
    GameLogger.debug(TAG, "copy(): Creating copy of fixture: $this.")
    return Fixture(
        shape.copy() as IGameShape2D,
        fixtureLabel,
        active,
        attachedToBody,
        Vector2(offsetFromBodyCenter),
        properties.copy())
  }

  override fun toString(): String {
    return "Fixture(shape=$shape, bodyRelativeShape=$bodyRelativeShape, fixtureLabel=$fixtureLabel, " +
        "active=$active, attachedToBody=$attachedToBody, offsetFromBodyCenter=$offsetFromBodyCenter, " +
        "properties=$properties)"
  }
}
