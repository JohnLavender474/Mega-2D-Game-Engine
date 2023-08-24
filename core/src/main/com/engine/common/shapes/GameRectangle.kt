package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

open class GameRectangle : Rectangle, GameShape2D {

  companion object {
    var OVERLAP_EXTENSION: ((GameRectangle, GameShape2D) -> Boolean)? = null
  }

  constructor() : super()

  constructor(x: Float, y: Float, width: Float, height: Float) : super(x, y, width, height)

  constructor(rect: Rectangle) : super(rect)

  fun getAsLines() =
      listOf(
          GameLine(getTopLeftPoint(), getTopRightPoint()),
          GameLine(getBottomLeftPoint(), getBottomRightPoint()),
          GameLine(getBottomLeftPoint(), getTopLeftPoint()),
          GameLine(getBottomRightPoint(), getTopRightPoint()))

  fun getMaxX() = x + width

  fun setMaxX(maxX: Float) = setX(maxX - width)

  fun getMaxY() = y + height

  fun setMaxY(maxY: Float) = setY(maxY - height)

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

  fun positionOnPoint(point: Vector2, position: Position): GameRectangle {
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

  fun getPositionPoint(position: Position) =
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

  fun setTopLeftToPoint(topLeftPoint: Vector2): Rectangle =
      setPosition(topLeftPoint.x, topLeftPoint.y - height)

  fun getTopLeftPoint() = Vector2(x, y + height)

  fun setTopCenterToPoint(topCenterPoint: Vector2): Rectangle =
      setPosition(topCenterPoint.x - (width / 2f), topCenterPoint.y - height)

  fun getTopCenterPoint() = Vector2(x + (width / 2f), y + height)

  fun setTopRightToPoint(topRightPoint: Vector2): Rectangle =
      setPosition(topRightPoint.x - width, topRightPoint.y - height)

  fun getTopRightPoint() = Vector2(x + width, y + height)

  fun setCenterLeftToPoint(centerLeftPoint: Vector2): Rectangle =
      this.setPosition(centerLeftPoint.x, centerLeftPoint.y - (this.height / 2f))

  fun getCenterLeftPoint(): Vector2 = Vector2(this.x, this.y + (this.height / 2f))

  fun setCenterToPoint(centerPoint: Vector2): Rectangle = this.setCenter(centerPoint)

  fun getCenterPoint(): Vector2 = this.getCenter(Vector2())

  fun setCenterRightToPoint(centerRightPoint: Vector2): Rectangle =
      this.setPosition(centerRightPoint.x - this.width, centerRightPoint.y - (this.height / 2f))

  fun getCenterRightPoint(): Vector2 = Vector2(this.x + this.width, this.y + (this.height / 2f))

  fun setBottomLeftToPoint(bottomLeftPoint: Vector2): Rectangle = this.setPosition(bottomLeftPoint)

  fun getBottomLeftPoint(): Vector2 = Vector2(this.x, this.y)

  fun setBottomCenterToPoint(bottomCenterPoint: Vector2): Rectangle =
      this.setPosition(bottomCenterPoint.x - this.width / 2f, bottomCenterPoint.y)

  fun getBottomCenterPoint(): Vector2 = Vector2(this.x + this.width / 2f, this.y)

  fun setBottomRightToPoint(bottomRightPoint: Vector2): Rectangle =
      this.setPosition(bottomRightPoint.x - this.width, bottomRightPoint.y)

  fun getBottomRightPoint(): Vector2 = Vector2(this.x + this.width, this.y)
}
