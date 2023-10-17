package com.engine.common.interfaces

/** An interface representing an object with a size, including width and height. */
interface Sizable {

  /**
   * Gets the width of the object.
   *
   * @return The width of the object.
   */
  fun getWidth(): Float

  /**
   * Gets the height of the object.
   *
   * @return The height of the object.
   */
  fun getHeight(): Float

  /**
   * Sets the size of the object by specifying both width and height.
   *
   * @param width The new width of the object.
   * @param height The new height of the object.
   */
  fun setSize(width: Float, height: Float) {
    setWidth(width)
    setHeight(height)
  }

  /**
   * Sets the width of the object.
   *
   * @param width The new width of the object.
   */
  fun setWidth(width: Float)

  /**
   * Sets the height of the object.
   *
   * @param height The new height of the object.
   */
  fun setHeight(height: Float)

  /**
   * Translates the size of the object by adding the specified width and height values.
   *
   * @param width The width value to add to the current width.
   * @param height The height value to add to the current height.
   */
  fun translateSize(width: Float, height: Float) = setSize(getWidth() + width, getHeight() + height)
}
