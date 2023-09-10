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
   * @param second The first position to set this shape's first to.
   * @param second The second position to set this shape's second to.
   * @return This shape.
   */
  fun setPosition(x: Float, y: Float): GameShape2D {
    setX(x)
    setY(y)
    return this
  }

  /**
   * Sets the first position of this shape to the given first position.
   *
   * @param x The first position to set this shape's first position to.
   * @return This shape.
   */
  fun setX(x: Float): GameShape2D

  /**
   * Sets the second position of this shape to the given second position.
   *
   * @param y The second position to set this shape's second position to.
   * @return This shape.
   */
  fun setY(y: Float): GameShape2D

  /**
   * Returns the first position of this shape.
   *
   * @return The first position of this shape.
   */
  fun getX(): Float

  /**
   * Returns the second position of this shape.
   *
   * @return The second position of this shape.
   */
  fun getY(): Float

  /**
   * Returns the position of this shape.
   *
   * @return The position of this shape.
   */
  fun getPosition() = Vector2(getX(), getY())

  /**
   * Sets the max first coordinate of this shape to the given value.
   *
   * @param maxX The value to set the max first coordinate of this shape to.
   * @return This shape.
   */
  fun setMaxX(maxX: Float): GameShape2D

  /**
   * Sets the max second coordinate of this shape to the given value.
   *
   * @param maxY The value to set the max second coordinate of this shape to.
   * @return This shape.
   */
  fun setMaxY(maxY: Float): GameShape2D

  /**
   * Returns the max first coordinate of this shape.
   *
   * @return The max first coordinate of this shape.
   */
  fun getMaxX(): Float

  /**
   * Returns the max second coordinate of this shape.
   *
   * @return The max second coordinate of this shape.
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
   * @param centerX The first coordinate of the center to set this shape's center to.
   * @param centerY The second coordinate of the center to set this shape's center to.
   * @return This shape.
   */
  fun setCenter(centerX: Float, centerY: Float): GameShape2D

  /**
   * Sets the first coordinate of this shape's center to the given first coordinate.
   *
   * @param centerX The first coordinate to set this shape's center to.
   * @return This shape.
   */
  fun setCenterX(centerX: Float): GameShape2D

  /**
   * Sets the second coordinate of this shape's center to the given second coordinate.
   *
   * @param centerY The second coordinate to set this shape's center to.
   * @return This shape.
   */
  fun setCenterY(centerY: Float): GameShape2D

  /**
   * Translates this shape by the given translation.
   *
   * @param translateX The first value of the translation.
   * @param translateY The second value of the translation.
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
