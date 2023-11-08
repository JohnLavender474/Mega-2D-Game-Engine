package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.ICopyable
import com.engine.common.interfaces.IPropertizable
import com.engine.common.objects.Properties
import com.engine.common.shapes.IGameShape2D
import com.engine.common.shapes.IGameShape2DSupplier

/**
 * A fixture for a [Body]. A fixture is a shape that is attached to a body. It can be used to detect
 * collisions with other fixtures.
 *
 * @param shape The shape of this fixture.
 * @param fixtureLabel The label for this fixture. Used to determine how this fixture interacts with
 *   other fixtures. It can be anything (String, Int, Enum, etc.) so long as it properly implements
 *   [Any.equals] and [Any.hashCode].
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
) : IGameShape2DSupplier, IPropertizable, ICopyable {

  override fun getGameShape2D() = shape

  /**
   * Returns if this fixture's shape overlaps the given shape.
   *
   * @return Whether this fixture's shape overlaps the given shape.
   * @see IGameShape2D.overlaps
   */
  fun overlaps(other: IGameShape2D) = shape.overlaps(other)

  /**
   * Returns if this fixture overlaps the given fixture. Specifically, this method returns if this
   * fixture's shape overlaps the given fixture's shape.
   *
   * @param other The fixture to check if this fixture overlaps.
   * @see overlaps
   */
  fun overlaps(other: Fixture) = overlaps(other.shape)

  /**
   * Creates a copy of this [Fixture].
   *
   * @return A copy of this [Fixture].
   */
  override fun copy() =
      Fixture(
          shape.copy() as IGameShape2D,
          fixtureLabel,
          active,
          attachedToBody,
          Vector2(offsetFromBodyCenter),
          properties.copy())

  override fun toString(): String {
    return "Fixture(shape=$shape, fixtureLabel=$fixtureLabel, active=$active, " +
        "attachedToBody=$attachedToBody, offsetFromBodyCenter=$offsetFromBodyCenter, " +
        "properties=$properties)"
  }
}
