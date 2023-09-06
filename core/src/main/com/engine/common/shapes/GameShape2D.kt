package com.engine.common.shapes

import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2

/**
 * A 2D shape that can be used in a game. This shape is a [Shape2D]. For convenience reasons, this
 * also extends [GameShape2DSupplier].
 *
 * @see Shape2D
 */
interface GameShape2D : Shape2D {

  /**
   * Returns a copy of this shape.
   *
   * @return A copy of this shape.
   */
  fun copy(): GameShape2D

  /**
   * Returns whether this shape overlaps the given shape.
   *
   * @param other The shape to check if this shape overlaps.
   * @return Whether this shape overlaps the given shape.
   */
  fun overlaps(other: GameShape2D): Boolean

  /**
   * Returns the bounding rectangle of this shape.
   *
   * @return The bounding rectangle of this shape.
   */
  fun getBoundingRectangle(): GameRectangle

  /**
   * Sets the position of this shape to the given position.
   *
   * @param position The position to set this shape's position to.
   * @return This shape.
   */
  fun setPosition(position: Vector2) = setPosition(position.x, position.y)

  /*
   * Sets the position of this shape to the given position.
   *
   * @param y The x position to set this shape's x to.
   * @param y The y position to set this shape's y to.
   * @return This shape.
   */
  fun setPosition(x: Float, y: Float): GameShape2D {
    setX(x)
    setY(y)
    return this
  }

  /**
   * Sets the x position of this shape to the given x position.
   *
   * @param x The x position to set this shape's x position to.
   * @return This shape.
   */
  fun setX(x: Float): GameShape2D

  /**
   * Sets the y position of this shape to the given y position.
   *
   * @param y The y position to set this shape's y position to.
   * @return This shape.
   */
  fun setY(y: Float): GameShape2D

  /**
   * Returns the x position of this shape.
   *
   * @return The x position of this shape.
   */
  fun getX(): Float

  /**
   * Returns the y position of this shape.
   *
   * @return The y position of this shape.
   */
  fun getY(): Float

  /**
   * Returns the position of this shape.
   *
   * @return The position of this shape.
   */
  fun getPosition() = Vector2(getX(), getY())

  /**
   * Sets the max x coordinate of this shape to the given value.
   *
   * @param maxX The value to set the max x coordinate of this shape to.
   * @return This shape.
   */
  fun setMaxX(maxX: Float): GameShape2D

  /**
   * Sets the max y coordinate of this shape to the given value.
   *
   * @param maxY The value to set the max y coordinate of this shape to.
   * @return This shape.
   */
  fun setMaxY(maxY: Float): GameShape2D

  /**
   * Returns the max x coordinate of this shape.
   *
   * @return The max x coordinate of this shape.
   */
  fun getMaxX(): Float

  /**
   * Returns the max y coordinate of this shape.
   *
   * @return The max y coordinate of this shape.
   */
  fun getMaxY(): Float

  /**
   * Returns the center of this shape.
   *
   * @return The center of this shape.
   */
  fun getCenter(): Vector2

  /**
   * Sets the center of this shape to the given center.
   *
   * @param center The center to set this shape's center to.
   * @return This shape.
   */
  fun setCenter(center: Vector2) = setCenter(center.x, center.y)

  /**
   * Sets the center of this shape to the given center.
   *
   * @param centerX The x coordinate of the center to set this shape's center to.
   * @param centerY The y coordinate of the center to set this shape's center to.
   * @return This shape.
   */
  fun setCenter(centerX: Float, centerY: Float): GameShape2D

  /**
   * Sets the x coordinate of this shape's center to the given x coordinate.
   *
   * @param centerX The x coordinate to set this shape's center to.
   * @return This shape.
   */
  fun setCenterX(centerX: Float): GameShape2D

  /**
   * Sets the y coordinate of this shape's center to the given y coordinate.
   *
   * @param centerY The y coordinate to set this shape's center to.
   * @return This shape.
   */
  fun setCenterY(centerY: Float): GameShape2D

  /**
   * Translates this shape by the given translation.
   *
   * @param translateX The x value of the translation.
   * @param translateY The y value of the translation.
   * @return This shape.
   */
  fun translation(translateX: Float, translateY: Float): GameShape2D

  /**
   * Translates this shape by the given translation.
   *
   * @param translation The translation to translate this shape by.
   * @return This shape.
   * @see translation
   */
  fun translation(translation: Vector2) = translation(translation.x, translation.y)
}
