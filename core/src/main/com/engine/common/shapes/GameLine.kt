package com.engine.common.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.engine.common.extensions.gdxArrayOf
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A line that can be used in a game. A line is defined by two pointsHandles.
 *
 * @param x1 The x-coordinate of the first point of this line.
 * @param y1 The y-coordinate of the first point of this line.
 * @param x2 The x-coordinate of the second point of this line.
 * @param y2 The y-coordinate of the second point of this line.
 */
class GameLine(x1: Float, y1: Float, x2: Float, y2: Float) : IGameShape2D {

  companion object {
    private var OVERLAP_EXTENSION: ((GameLine, IGameShape2D) -> Boolean)? = null

    /**
     * Sets the overlap extension function to the given function. This function will be called when
     * [GameLine.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle] or
     * [GameLine]. This function should return true if the given [IGameShape2D] overlaps this
     * [GameLine] and false otherwise.
     *
     * @param overlapExtension The function to call when [GameLine.overlaps] is called with a
     *   [IGameShape2D] that is not a [GameRectangle] or [GameLine].
     */
    fun setOverlapExtension(overlapExtension: (GameLine, IGameShape2D) -> Boolean) {
      OVERLAP_EXTENSION = overlapExtension
    }
  }

  override var color: Color = Color.RED
  override var thickness: Float = 1f
  override var shapeType: ShapeType = ShapeType.Filled

  private val localPoint1 = Vector2()
  private val localPoint2 = Vector2()
  private val position = Vector2()
  private val worldPoint1 = Vector2()
  private val worldPoint2 = Vector2()

  var scale = 0f
    set(value) {
      field = value
      dirty = true
    }

  var rotation = 0f
    set(value) {
      field = value
      dirty = true
    }

  var originX = 0f
    set(value) {
      field = value
      dirty = true
    }

  var originY = 0f
    set(value) {
      field = value
      dirty = true
    }

  private var length = 0f
  private var dirty = true
  private var calculateLength = true

  init {
    localPoint1.x = x1
    localPoint1.y = y1
    localPoint2.x = x2
    localPoint2.y = y2
  }

  /**
   * Creates a new line with the given line.
   *
   * @param line The line to copy.
   * @return A line with the given pointsHandles.
   */
  constructor(line: GameLine) : this(line.localPoint1, line.localPoint2) {
    scale = line.scale
    rotation = line.rotation
    originX = line.originX
    originY = line.originY
  }

  /**
   * Creates a line with the given points.
   *
   * @param point1 The first point of this line.
   * @param point2 The second point of this line.
   */
  constructor(point1: Vector2, point2: Vector2) : this(point1.x, point1.y, point2.x, point2.y)

  /** Creates a line with points at [0,0] and [0,0] */
  constructor() : this(0f, 0f, 0f, 0f)

  /**
   * Calculates and returns the length of this line.
   *
   * @return The length of this line.
   */
  fun getLength(): Float {
    if (calculateLength) {
      calculateLength = false
      val x = localPoint1.x - localPoint2.x
      val y = localPoint1.y - localPoint2.y
      length = sqrt((x * x + y * y).toDouble()).toFloat()
    }
    return length
  }

  /**
   * Sets the local points (unscaled, unrotated, etc.) of this line.
   *
   * @param x1 The x-coordinate of the first point of this line.
   * @param y1 The y-coordinate of the first point of this line.
   * @param x2 The x-coordinate of the second point of this line.
   * @param y2 The y-coordinate of the second point of this line.
   * @return This shape for chaining
   */
  fun setLocalPoints(x1: Float, y1: Float, x2: Float, y2: Float): GameLine {
    localPoint1.set(x1, y1)
    localPoint2.set(x2, y2)
    dirty = true
    calculateLength = true
    return this
  }

  /**
   * Sets the local points (unscaled, unrotated, etc.) of this line.
   *
   * @param point1 The first point of this line.
   * @param point2 The second point of this line.
   * @return This shape for chaining
   */
  fun setLocalPoints(point1: Vector2, point2: Vector2) =
      setLocalPoints(point1.x, point1.y, point2.x, point2.y)

  /**
   * Gets the local points (unscaled, unrotated, etc.) of this line.
   *
   * @return The local points of this line.
   */
  fun getLocalPoints() = Pair(Vector2(localPoint1), Vector2(localPoint2))

  /**
   * Gets the world points (scaled, rotated, etc.) of this line.
   *
   * @return The world points of this line.
   */
  fun getWorldPoints(): Pair<Vector2, Vector2> {
    if (!dirty) return Pair(Vector2(worldPoint1), Vector2(worldPoint2))
    dirty = false

    val cos = MathUtils.cosDeg(rotation)
    val sin = MathUtils.sinDeg(rotation)

    var first = true
    gdxArrayOf(localPoint1, localPoint2).forEach {
      var x = it.x - originX
      var y = it.y - originY

      if (rotation != 0f) {
        val oldX = x
        x = cos * x - sin * y
        y = sin * oldX + cos * y
      }

      val worldPoint = if (first) worldPoint1 else worldPoint2
      first = false

      worldPoint.x = position.x + x + originX
      worldPoint.y = position.y + y + originY
    }

    return Pair(Vector2(worldPoint1), Vector2(worldPoint2))
  }

  /**
   * Checks if the point is contained in this line. The world points are used for calculating the
   * containment via [getWorldPoints].
   *
   * @param point The point to check.
   * @return True if the point is contained in this line.
   */
  override fun contains(point: Vector2): Boolean {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()
    return Intersector.pointLineSide(_worldPoint1, _worldPoint2, point) == 0 &&
        point.x <= getMaxX() &&
        point.x >= getX()
  }

  /**
   * Checks if the point is contained in this line. The world points are used for calculating the
   * containment via [getWorldPoints].
   *
   * @param x The first coordinate of the point to check.
   * @param y The second coordinate of the point to check.
   * @return True if the point is contained in this line.
   * @see contains
   */
  override fun contains(x: Float, y: Float) = contains(Vector2(x, y))

  /**
   * Draws this line using the world points.
   *
   * @return The drawer.
   */
  override fun draw(drawer: ShapeRenderer) {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()
    drawer.line(_worldPoint1, _worldPoint2)
  }

  /**
   * Centers the world coordinates of the line on the given point.
   *
   * @param center The point to center the world coordinates of the line on.
   * @return This line.
   */
  override fun setCenter(center: Vector2): GameLine {
    val currentCenter = getCenter()
    val centerDeltaX = center.x - currentCenter.x
    val centerDeltaY = center.y - currentCenter.y

    if (centerDeltaX == 0f && centerDeltaY == 0f) return this

    position.x += centerDeltaX
    position.y += centerDeltaY
    localPoint1.x += centerDeltaX
    localPoint1.y += centerDeltaY
    localPoint2.x += centerDeltaX
    localPoint2.y += centerDeltaY

    dirty = true
    calculateLength = true
    return this
  }

  /**
   * Returns the center of the world points.
   *
   * @return The center of the world points.
   */
  override fun getCenter(): Vector2 {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()
    return Vector2((_worldPoint1.x + _worldPoint2.x) / 2f, (_worldPoint1.y + _worldPoint2.y) / 2f)
  }

  /**
   * Returns the center of the local points.
   *
   * @return The center of the local points.
   */
  fun getLocalCenter() =
      Vector2((localPoint1.x + localPoint2.x) / 2f, (localPoint1.y + localPoint2.y) / 2f)

  /**
   * Sets the x-coordinate of the first point of this line.
   *
   * @param x The new x-coordinate of the first point of this line.
   * @return This line.
   */
  override fun setX(x: Float): GameLine {
    if (position.x != x) {
      position.x = x
      dirty = true
    }
    return this
  }

  /**
   * Sets the y-coordinate of the first point of this line.
   *
   * @param y The new y-coordinate of the first point of this line.
   * @return This line.
   */
  override fun setY(y: Float): GameLine {
    if (position.y != y) {
      position.y = y
      dirty = true
    }
    return this
  }

  /**
   * Returns the x position of the line.
   *
   * @return The x position of the line.
   */
  override fun getX() = position.x

  /**
   * Returns the y position of the line.
   *
   * @return The y position of the line.
   */
  override fun getY() = position.y

  /**
   * Returns the max x of the line using the world points via [getWorldPoints].
   *
   * @return The max x of the line.
   */
  override fun getMaxX(): Float {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()
    return max(_worldPoint1.x, _worldPoint2.x)
  }

  /**
   * Returns the max y of the line using the world points via [getWorldPoints].
   *
   * @return The max y of the line.
   */
  override fun getMaxY(): Float {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()
    return max(_worldPoint1.y, _worldPoint2.y)
  }

  /**
   * Sets the origin of this line.
   *
   * @param origin The origin.
   * @return This line.
   */
  fun setOrigin(origin: Vector2) = setOrigin(origin.x, origin.y)

  /**
   * Sets the origin of this line.
   *
   * @param originX The x-coordinate of the origin.
   * @param originY The y-coordinate of the origin.
   * @return This line.
   */
  fun setOrigin(originX: Float, originY: Float): GameLine {
    this.originX = originX
    this.originY = originY
    dirty = true
    return this
  }

  /**
   * Translates the position of this line.
   *
   * @param translateX The amount to translate the x-coordinate of this line.
   * @param translateY The amount to translate the y-coordinate of this line.
   * @return This line.
   */
  override fun translation(translateX: Float, translateY: Float): GameLine {
    position.x += translateX
    position.y += translateY
    dirty = true
    return this
  }

  /**
   * Returns a copy of this line.
   *
   * @return A copy of this line.
   */
  override fun copy(): GameLine = GameLine(this)

  /**
   * Returns true if the provided [IGameShape2D] overlaps this game line. The world points are used
   * for calculating the overlap via [getWorldPoints].
   *
   * @return True if the provided [IGameShape2D] overlaps this game line.
   */
  override fun overlaps(other: IGameShape2D): Boolean {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()

    return when (other) {
      is GameRectangle -> Intersector.intersectSegmentRectangle(_worldPoint1, _worldPoint2, other)
      is GameCircle ->
          Intersector.intersectSegmentCircle(
              _worldPoint1, _worldPoint2, other.getCenter(), other.getRadius() * other.getRadius())
      is GameLine -> {
        val (otherWorldPoint1, otherWorldPoint2) = other.getWorldPoints()
        Intersector.intersectSegments(
            _worldPoint1, _worldPoint2, otherWorldPoint1, otherWorldPoint2, null)
      }
      else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
    }
  }

  /**
   * Returns the bounding rectangle of this line. The world points are used for calculating the
   * bounding rectangle via [getWorldPoints].
   *
   * @return The bounding rectangle of this line.
   */
  override fun getBoundingRectangle(): GameRectangle {
    val (_worldPoint1, _worldPoint2) = getWorldPoints()

    val minX = min(_worldPoint1.x, _worldPoint2.x)
    val maxX = max(_worldPoint1.x, _worldPoint2.x)
    val minY = min(_worldPoint1.y, _worldPoint2.y)
    val maxY = max(_worldPoint1.y, _worldPoint2.y)

    return GameRectangle(minX, minY, maxX - minX, maxY - minY)
  }
}
