package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import kotlin.math.max
import kotlin.math.min

/**
 * A line that can be used in a game. A line is defined by two points.
 *
 * @param point1 The first point of this line.
 * @param point2 The second point of this line.
 */
class GameLine(val point1: Vector2, val point2: Vector2) : GameShape2D {

  companion object {
    private var OVERLAP_EXTENSION: ((GameLine, GameShape2D) -> Boolean)? = null

    /**
     * Sets the overlap extension function to the given function. This function will be called when
     * [GameLine.overlaps] is called with a [GameShape2D] that is not a [GameRectangle] or
     * [GameLine]. This function should return true if the given [GameShape2D] overlaps this
     * [GameLine] and false otherwise.
     *
     * @param overlapExtension The function to call when [GameLine.overlaps] is called with a
     *   [GameShape2D] that is not a [GameRectangle] or [GameLine].
     */
    fun setOverlapExtension(overlapExtension: (GameLine, GameShape2D) -> Boolean) {
      OVERLAP_EXTENSION = overlapExtension
    }
  }

  /** Creates a line with the given points. */
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

  override fun setX(x: Float): GameShape2D {
    val pointToMove = if (point1.x < point2.x) point1 else point2
    val translateX = x - pointToMove.x
    return translation(translateX, 0f)
  }

  override fun setY(y: Float): GameShape2D {
    val pointToMove = if (point1.y < point2.y) point1 else point2
    val translateY = y - pointToMove.y
    return translation(0f, translateY)
  }

  override fun getX() = min(point1.x, point2.x)

  override fun getY() = min(point1.y, point2.y)

  override fun getMaxX() = max(point1.x, point2.x)

  override fun getMaxY() = max(point1.y, point2.y)

  override fun setMaxX(maxX: Float) = translation(maxX - getMaxX(), 0f)

  override fun setMaxY(maxY: Float) = translation(0f, maxY - getMaxY())

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
