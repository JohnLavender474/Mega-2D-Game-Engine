package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import kotlin.math.max
import kotlin.math.min

class GameLine(val point1: Vector2, val point2: Vector2) : GameShape2D {

  companion object {
    var OVERLAP_EXTENSION: ((GameLine, GameShape2D) -> Boolean)? = null
  }

  constructor(x1: Float, y1: Float, x2: Float, y2: Float) : this(Vector2(x1, y1), Vector2(x2, y2))

  override fun contains(point: Vector2) = Intersector.pointLineSide(point1, point2, point) == 0

  override fun contains(x: Float, y: Float) = contains(Vector2(x, y))

  override fun getCenter() = Vector2((point1.x + point2.x) / 2f, (point1.y + point2.y) / 2f)

  override fun setCenterX(centerX: Float): GameLine {
    val currCenterX = getCenter().x
    val translateX = centerX - currCenterX
    translation(translateX, 0f)
    return this
  }

  override fun setCenterY(centerY: Float): GameLine {
    val currCenterY = getCenter().y
    val translateY = centerY - currCenterY
    translation(0f, translateY)
    return this
  }

  override fun setCenter(centerX: Float, centerY: Float): GameLine {
    setCenterX(centerX)
    setCenterY(centerY)
    return this
  }

  fun getMaxX() = max(point1.x, point2.x)

  fun getMaxY() = max(point1.y, point2.y)

  fun setMaxX(maxX: Float) {
    val currMaxX = getMaxX()
    val translateX = maxX - currMaxX
    translation(translateX, 0f)
  }

  fun setMaxY(maxY: Float) {
    val currMaxY = getMaxY()
    val translateY = maxY - currMaxY
    translation(0f, translateY)
  }

  override fun translation(translateX: Float, translateY: Float): GameLine {
    point1.x += translateX
    point2.x += translateX
    point1.y += translateY
    point2.y += translateY
    return this
  }

  override fun overlaps(other: GameShape2D) =
      when (other) {
        is GameRectangle -> Intersector.intersectSegmentRectangle(point1, point2, other)
        is GameLine ->
            Intersector.intersectLines(point1, point2, other.point1, other.point2, Vector2())
        else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
      }

  override fun getBoundingRectangle(): GameRectangle {
    val minX = min(point1.x, point2.x)
    val maxX = max(point1.x, point2.x)
    val minY = min(point1.y, point2.y)
    val maxY = max(point1.y, point2.y)
    return GameRectangle(minX, minY, maxX - minX, maxY - minY)
  }
}
