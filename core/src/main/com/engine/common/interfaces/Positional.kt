package com.engine.common.interfaces

import com.badlogic.gdx.math.Vector2

/** An interface for objects that have a position. */
interface Positional {

  /**
   * Gets the x-coordinate of this object.
   *
   * @return the x-coordinate of this object
   */
  fun getX(): Float

  /**
   * Gets the y-coordinate of this object.
   *
   * @return the y-coordinate of this object
   */
  fun getY(): Float

  /**
   * Gets the position of this object.
   *
   * @return the position of this object
   */
  fun getPosition() = Vector2(getX(), getY())

  /**
   * Sets the position of this object.
   *
   * @param x the x-position to set
   * @param y the y-position to set
   */
  fun setPosition(x: Float, y: Float)
}
