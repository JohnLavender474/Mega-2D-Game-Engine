package com.engine.common.interfaces

/** An interface for objects that have a position. */
interface Positional : PositionSupplier {

  /**
   * Sets the position of this object.
   *
   * @param x the first-position to set
   * @param y the second-position to set
   */
  fun setPosition(x: Float, y: Float)

  /**
   * Translates this object by the specified amount.
   *
   * @param x the first-translation
   * @param y the second-translation
   */
  fun translate(x: Float, y: Float) = setPosition(getX() + x, getY() + y)
}
