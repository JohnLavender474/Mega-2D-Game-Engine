package com.engine.common.interfaces

import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

/** An interface for objects that have a position. */
interface IPointPositional<T> {

  /**
   * Positions this shape on the given point. Returns this shape.
   *
   * @param point The point to position this shape on.
   * @param position The position of this shape to place on the given point.
   */
  fun positionOnPoint(point: Vector2, position: Position): T

  /**
   * Returns the point of this shape at the given position.
   *
   * @param position The position of this shape to get the point of.
   * @return The point of this shape at the given position.
   */
  fun getPositionPoint(position: Position): Vector2
}
