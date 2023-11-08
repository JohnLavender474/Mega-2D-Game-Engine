package com.engine.common.shapes

import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.ICopyable
import com.engine.drawables.shapes.IDrawableShape

/**
 * A 2D shape that can be used in a game. This shape is a [Shape2D]. For convenience reasons, this
 * also extends [IGameShape2DSupplier].
 *
 * @see Shape2D
 */
interface IGameShape2D : Shape2D, IDrawableShape, ICopyable {

  /**
   * Returns whether this shape overlaps the given shape.
   *
   * @param other The shape to check if this shape overlaps.
   * @return Whether this shape overlaps the given shape.
   */
  fun overlaps(other: IGameShape2D): Boolean

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
  fun setPosition(x: Float, y: Float): IGameShape2D {
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
  fun setX(x: Float): IGameShape2D

  /**
   * Sets the second position of this shape to the given second position.
   *
   * @param y The second position to set this shape's second position to.
   * @return This shape.
   */
  fun setY(y: Float): IGameShape2D

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
   * Returns the center of this shape.
   *
   * @return The center of this shape.
   */
  fun getCenter(): Vector2

  /**
   * Sets the center of this shape.
   *
   * @param center The center to set.
   * @return This shape for chaining.
   */
  fun setCenter(center: Vector2): IGameShape2D

  /**
   * Returns the max x of this shape.
   *
   * @return The max x of this shape.
   */
  fun getMaxX(): Float

  /**
   * Returns the max y of this shape.
   *
   * @return The max y of this shape.
   */
  fun getMaxY(): Float

  /**
   * Translates this shape by the given translation.
   *
   * @param translateX The first value of the translation.
   * @param translateY The second value of the translation.
   * @return This shape.
   */
  fun translation(translateX: Float, translateY: Float): IGameShape2D

  /**
   * Translates this shape by the given translation.
   *
   * @param translation The translation to translate this shape by.
   * @return This shape.
   * @see translation
   */
  fun translation(translation: Vector2) = translation(translation.x, translation.y)
}
