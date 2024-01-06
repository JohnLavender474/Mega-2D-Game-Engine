package com.engine.common.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.ICopyable
import com.engine.drawables.shapes.IDrawableShape

/**
 * A 2D shape that can be used in a game. This shape is a [Shape2D]. [Shape2D] objects are expected
 * to implement the same signatures defined in [Shape2D] as well as those in [IDrawableShape] (for
 * drawing this shape with [ShapeRenderer]), ICopyable (for returning a copy of this shape), and
 * [ICardinallyRotatableShape2D] since it is expected that any shape can be rotated in 90 degree
 * increment around an origin.
 *
 * @property originX used to determine the origin of this shape when rotating it. This is the first
 *   value of the origin.
 * @property originY used to determine the origin of this shape when rotating it. This is the second
 *   value of the origin.
 * @see Shape2D
 */
interface IGameShape2D : Shape2D, IDrawableShape, ICopyable, ICardinallyRotatableShape2D {

  var originX: Float
  var originY: Float

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
   * Sets the center of this shape.
   *
   * @param centerX The first value of the center.
   * @param centerY The second value of the center.
   * @return This shape for chaining.
   */
  fun setCenter(centerX: Float, centerY: Float): IGameShape2D

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
