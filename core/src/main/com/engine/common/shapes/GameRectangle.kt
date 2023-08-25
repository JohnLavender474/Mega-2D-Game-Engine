package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

/**
 * A [GameRectangle] is a [Rectangle] that implements [PositionalGameShape2D]. It is used to
 * represent a rectangle in a game.
 *
 * @see Rectangle
 * @see PositionalGameShape2D
 */
open class GameRectangle : Rectangle, PositionalGameShape2D {

  companion object {
    private var OVERLAP_EXTENSION: ((GameRectangle, GameShape2D) -> Boolean)? = null

    /**
     * Sets the overlap extension function to the given function. This function will be called when
     * [GameRectangle.overlaps] is called with a [GameShape2D] that is not a [GameRectangle] or
     * [GameLine]. This function should return true if the given [GameShape2D] overlaps this
     * [GameRectangle] and false otherwise.
     *
     * @param overlapExtension The function to call when [GameRectangle.overlaps] is called with a
     *   [GameShape2D] that is not a [GameRectangle] or [GameLine].
     */
    fun setOverlapExtension(overlapExtension: (GameRectangle, GameShape2D) -> Boolean) {
      OVERLAP_EXTENSION = overlapExtension
    }
  }

  /**
   * Creates a new [GameRectangle] with all values set to 0.
   *
   * @see Rectangle
   */
  constructor() : super()

  /**
   * Creates a new [GameRectangle] with the given x, y, width, and height.
   *
   * @param x The x coordinate of the bottom left corner of this rectangle.
   * @param y The y coordinate of the bottom left corner of this rectangle.
   * @param width The width of this rectangle.
   * @param height The height of this rectangle.
   * @see Rectangle
   */
  constructor(x: Float, y: Float, width: Float, height: Float) : super(x, y, width, height)

  /**
   * Creates a new [GameRectangle] with the given [Rectangle].
   *
   * @param rect The [Rectangle] to create this [GameRectangle] from.
   * @see Rectangle
   */
  constructor(rect: Rectangle) : super(rect)

  /**
   * Returns a list of [GameLine]s that make up this [GameRectangle].
   *
   * @return A list of [GameLine]s that make up this [GameRectangle].
   */
  fun getAsLines() =
      listOf(
          GameLine(getTopLeftPoint(), getTopRightPoint()),
          GameLine(getBottomLeftPoint(), getBottomRightPoint()),
          GameLine(getBottomLeftPoint(), getTopLeftPoint()),
          GameLine(getBottomRightPoint(), getTopRightPoint()))

  /**
   * Sets the size of this [GameRectangle] to the given size. Returns this [GameRectangle].
   *
   * @param sizeXY The size to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setSize
   */
  override fun setSize(sizeXY: Float) = super.setSize(sizeXY) as GameRectangle

  /**
   * Sets the position of this [GameRectangle] to the given x, y, width and height. Returns this
   *
   * @param x The x coordinate to set this [GameRectangle] to.
   * @param y The y coordinate to set this [GameRectangle] to.
   * @param width The width to set this [GameRectangle] to.
   * @param height The height to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.set
   */
  override fun set(x: Float, y: Float, width: Float, height: Float) =
      super.set(x, y, width, height) as GameRectangle

  /**
   * Sets this [GameRectangle] to the given [Rectangle]. Returns this [GameRectangle].
   *
   * @param rect The [Rectangle] to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.set
   */
  override fun set(rect: Rectangle) = super.set(rect) as GameRectangle

  /**
   * Sets the x coordinate of the bottom left corner of this [GameRectangle] to the given value.
   * Returns this [GameRectangle].
   *
   * @param x The x coordinate to set the bottom left corner of this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setX
   */
  override fun setX(x: Float) = super<Rectangle>.setX(x) as GameRectangle

  /**
   * Sets the y coordinate of the bottom left corner of this [GameRectangle] to the given value.
   * Returns this [GameRectangle].
   *
   * @param y The y coordinate to set the bottom left corner of this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setY
   */
  override fun setY(y: Float) = super<Rectangle>.setY(y) as GameRectangle

  /**
   * Sets the width of this [GameRectangle] to the given value. Returns this [GameRectangle].
   *
   * @param width The width to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setWidth
   */
  override fun setWidth(width: Float) = super.setWidth(width) as GameRectangle

  /**
   * Sets the height of this [GameRectangle] to the given value. Returns this [GameRectangle].
   *
   * @param height The height to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setHeight
   */
  override fun setHeight(height: Float) = super.setHeight(height) as GameRectangle

  /**
   * Sets the position of this [GameRectangle] to the given position. Returns this [GameRectangle].
   *
   * @param position The position to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setPosition
   */
  override fun setPosition(position: Vector2) = super.setPosition(position) as GameRectangle

  /**
   * Sets the position of this [GameRectangle] to the given x and y. Returns this [GameRectangle].
   *
   * @param x The x coordinate to set this [GameRectangle] to.
   * @param y The y coordinate to set this [GameRectangle] to.
   * @return This [GameRectangle].
   * @see Rectangle.setPosition
   */
  override fun setPosition(x: Float, y: Float) = super.setPosition(x, y) as GameRectangle

  /**
   * Merge this [GameRectangle] with the given [Rectangle]. Returns this [GameRectangle].
   *
   * @param rect The [Rectangle] to merge this [GameRectangle] with.
   * @return This [GameRectangle].
   * @see Rectangle.merge
   */
  override fun merge(rect: Rectangle) = super.merge(rect) as GameRectangle

  /**
   * Merge this [GameRectangle] with the given x and y. Returns this [GameRectangle].
   *
   * @param x The x coordinate to merge this [GameRectangle] with.
   * @return This [GameRectangle].
   * @see Rectangle.merge
   */
  override fun merge(x: Float, y: Float) = super.merge(x, y) as GameRectangle

  /**
   * Merge this [GameRectangle] with the given [Vector2]. Returns this [GameRectangle].
   *
   * @param vec The [Vector2] to merge this [GameRectangle] with.
   * @return This [GameRectangle].
   * @see Rectangle.merge
   */
  override fun merge(vec: Vector2) = super.merge(vec) as GameRectangle

  /**
   * Merge this [GameRectangle] with the given [Vector2]s. Returns this [GameRectangle].
   *
   * @param vecs The [Vector2]s to merge this [GameRectangle] with.
   * @return This [GameRectangle].
   * @see Rectangle.merge
   */
  override fun merge(vecs: Array<out Vector2>) = super.merge(vecs) as GameRectangle

  /**
   * Fit this [GameRectangle] inside the given [Rectangle]. Returns this [GameRectangle].
   *
   * @param rect The [Rectangle] to fit this [GameRectangle] inside.
   * @return This [GameRectangle].
   * @see Rectangle.fitInside
   */
  override fun fitOutside(rect: Rectangle) = super.fitOutside(rect) as GameRectangle

  /**
   * Fit this [GameRectangle] inside the given [Rectangle]. Returns this [GameRectangle].
   *
   * @param rect The [Rectangle] to fit this [GameRectangle] inside.
   * @return This [GameRectangle].
   * @see Rectangle.fitInside
   */
  override fun fitInside(rect: Rectangle) = super.fitInside(rect) as GameRectangle

  /**
   * Set from string. Returns this [GameRectangle].
   *
   * @return This [GameRectangle].
   * @see Rectangle.fromString
   */
  override fun fromString(v: String) = super.fromString(v) as GameRectangle

  override fun setMaxX(maxX: Float) = setX(maxX - width)

  override fun setMaxY(maxY: Float) = setY(maxY - height)

  override fun getMaxX() = x + width

  override fun getMaxY() = y + height

  override fun overlaps(other: GameShape2D) =
      when (other) {
        is GameRectangle -> Intersector.overlaps(this, other)
        is GameLine -> Intersector.intersectSegmentRectangle(other.point1, other.point2, this)
        else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
      }

  override fun getBoundingRectangle() = GameRectangle(this)

  override fun getCenter(): Vector2 = super.getCenter(Vector2())

  override fun setCenter(centerX: Float, centerY: Float): GameRectangle {
    setCenterX(centerX)
    setCenterY(centerY)
    return this
  }

  override fun setCenter(center: Vector2): GameRectangle = setCenter(center.x, center.y)

  override fun setCenterX(centerX: Float): GameRectangle {
    super<Rectangle>.setCenter(centerX, getCenter().y)
    return this
  }

  override fun setCenterY(centerY: Float): GameRectangle {
    super<Rectangle>.setCenter(getCenter().x, centerY)
    return this
  }

  override fun translation(translateX: Float, translateY: Float): GameRectangle {
    x += translateX
    y += translateY
    return this
  }

  override fun positionOnPoint(point: Vector2, position: Position): GameRectangle {
    when (position) {
      Position.TOP_LEFT -> setTopLeftToPoint(point)
      Position.TOP_CENTER -> setTopCenterToPoint(point)
      Position.TOP_RIGHT -> setTopRightToPoint(point)
      Position.CENTER_LEFT -> setCenterLeftToPoint(point)
      Position.CENTER -> setCenterToPoint(point)
      Position.CENTER_RIGHT -> setCenterRightToPoint(point)
      Position.BOTTOM_LEFT -> setBottomLeftToPoint(point)
      Position.BOTTOM_CENTER -> setBottomCenterToPoint(point)
      Position.BOTTOM_RIGHT -> setBottomRightToPoint(point)
    }
    return this
  }

  override fun getPositionPoint(position: Position) =
      when (position) {
        Position.TOP_LEFT -> getTopLeftPoint()
        Position.TOP_CENTER -> getTopCenterPoint()
        Position.TOP_RIGHT -> getTopRightPoint()
        Position.CENTER_LEFT -> getCenterLeftPoint()
        Position.CENTER -> getCenterPoint()
        Position.CENTER_RIGHT -> getCenterRightPoint()
        Position.BOTTOM_LEFT -> getBottomLeftPoint()
        Position.BOTTOM_CENTER -> getBottomCenterPoint()
        Position.BOTTOM_RIGHT -> getBottomRightPoint()
      }

  /**
   * Sets the top left corner of this [GameRectangle] to the given point. Returns this
   * [GameRectangle].
   *
   * @param topLeftPoint The point to set the top left corner of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setTopLeftToPoint(topLeftPoint: Vector2): GameRectangle =
      setPosition(topLeftPoint.x, topLeftPoint.y - height)

  /**
   * Returns the top left corner of this [GameRectangle].
   *
   * @return The top left corner of this [GameRectangle].
   */
  fun getTopLeftPoint() = Vector2(x, y + height)

  /**
   * Sets the top center of this [GameRectangle] to the given point. Returns this [GameRectangle].
   *
   * @param topCenterPoint The point to set the top center of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setTopCenterToPoint(topCenterPoint: Vector2): GameRectangle =
      setPosition(topCenterPoint.x - (width / 2f), topCenterPoint.y - height)

  /**
   * Returns the top center of this [GameRectangle].
   *
   * @return The top center of this [GameRectangle].
   */
  fun getTopCenterPoint() = Vector2(x + (width / 2f), y + height)

  /**
   * Sets the top right corner of this [GameRectangle] to the given point. Returns this
   * [GameRectangle].
   *
   * @param topRightPoint The point to set the top right corner of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setTopRightToPoint(topRightPoint: Vector2): GameRectangle =
      setPosition(topRightPoint.x - width, topRightPoint.y - height)

  /**
   * Returns the top right corner of this [GameRectangle].
   *
   * @return The top right corner of this [GameRectangle].
   */
  fun getTopRightPoint() = Vector2(x + width, y + height)

  /**
   * Sets the center left of this [GameRectangle] to the given point. Returns this [GameRectangle].
   *
   * @param centerLeftPoint The point to set the center left of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setCenterLeftToPoint(centerLeftPoint: Vector2): GameRectangle =
      this.setPosition(centerLeftPoint.x, centerLeftPoint.y - (this.height / 2f))

  /**
   * Returns the center left of this [GameRectangle].
   *
   * @return The center left of this [GameRectangle].
   */
  fun getCenterLeftPoint(): Vector2 = Vector2(this.x, this.y + (this.height / 2f))

  /**
   * Sets the center of this [GameRectangle] to the given point. Returns this [GameRectangle].
   *
   * @param centerPoint The point to set the center of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setCenterToPoint(centerPoint: Vector2): GameRectangle = this.setCenter(centerPoint)

  /**
   * Returns the center of this [GameRectangle].
   *
   * @return The center of this [GameRectangle].
   * @see Rectangle.getCenter
   */
  fun getCenterPoint(): Vector2 = this.getCenter(Vector2())

  /**
   * Sets the center right of this [GameRectangle] to the given point. Returns this [GameRectangle].
   *
   * @param centerRightPoint The point to set the center right of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setCenterRightToPoint(centerRightPoint: Vector2): GameRectangle =
      this.setPosition(centerRightPoint.x - this.width, centerRightPoint.y - (this.height / 2f))

  /**
   * Returns the center right of this [GameRectangle].
   *
   * @return The center right of this [GameRectangle].
   */
  fun getCenterRightPoint(): Vector2 = Vector2(this.x + this.width, this.y + (this.height / 2f))

  /**
   * Sets the bottom left of this [GameRectangle] to the given point. Returns this [GameRectangle].
   *
   * @param bottomLeftPoint The point to set the bottom left of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setBottomLeftToPoint(bottomLeftPoint: Vector2): GameRectangle =
      this.setPosition(bottomLeftPoint)

  /**
   * Returns the bottom left of this [GameRectangle].
   *
   * @return The bottom left of this [GameRectangle].
   */
  fun getBottomLeftPoint(): Vector2 = Vector2(this.x, this.y)

  /**
   * Sets the bottom center of this [GameRectangle] to the given point. Returns this
   * [GameRectangle].
   *
   * @param bottomCenterPoint The point to set the bottom center of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setBottomCenterToPoint(bottomCenterPoint: Vector2): GameRectangle =
      this.setPosition(bottomCenterPoint.x - this.width / 2f, bottomCenterPoint.y)

  /**
   * Returns the bottom center of this [GameRectangle].
   *
   * @return The bottom center of this [GameRectangle].
   */
  fun getBottomCenterPoint(): Vector2 = Vector2(this.x + this.width / 2f, this.y)

  /**
   * Sets the bottom right of this [GameRectangle] to the given point. Returns this [GameRectangle].
   *
   * @param bottomRightPoint The point to set the bottom right of this [GameRectangle] to.
   * @return This [GameRectangle].
   */
  fun setBottomRightToPoint(bottomRightPoint: Vector2): GameRectangle =
      this.setPosition(bottomRightPoint.x - this.width, bottomRightPoint.y)

  /**
   * Returns the bottom right of this [GameRectangle].
   *
   * @return The bottom right of this [GameRectangle].
   */
  fun getBottomRightPoint(): Vector2 = Vector2(this.x + this.width, this.y)
}
