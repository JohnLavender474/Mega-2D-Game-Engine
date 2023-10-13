package com.engine.common

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.engine.common.enums.Direction
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object UtilMethods {

  private val RAND = Random(System.currentTimeMillis())

  fun getRandom(min: Int, max: Int): Int {
    return RAND.nextInt(max + 1 - min) + min
  }

  fun doIfRandMatch(min: Int, max: Int, matches: Iterable<Int>, runOnMatch: Consumer<Int?>) {
    val r = getRandom(min, max)
    for (i in matches) {
      if (r == i) {
        runOnMatch.accept(r)
        break
      }
    }
  }

  fun <T> mask(o1: T, o2: T, p1: Predicate<T>, p2: Predicate<T>): Boolean {
    return p1.test(o1) && p2.test(o2) || p2.test(o1) && p1.test(o2)
  }

  fun getSingleMostDirectionFromStartToTarget(start: Rectangle, target: Rectangle): Direction {
    val startCenter = start.getCenter(Vector2())
    val targetCenter = target.getCenter(Vector2())
    return getSingleMostDirectionFromStartToTarget(startCenter, targetCenter)
  }

  fun getSingleMostDirectionFromStartToTarget(start: Vector2, target: Vector2): Direction {
    val x = target.x - start.x
    val y = target.y - start.y
    return if (abs(x.toDouble()) > abs(y.toDouble())) {
      if (x > 0) Direction.RIGHT else Direction.LEFT
    } else {
      if (y > 0) Direction.UP else Direction.DOWN
    }
  }

  fun getSlope(p1: Vector2, p2: Vector2): Float {
    return (p1.y - p2.y) / (p1.x - p2.x)
  }

  fun normalizedTrajectory(start: Vector2, end: Vector2, speed: Float): Vector2 {
    var x = end.x - start.x
    var y = end.y - start.y
    val length = sqrt((x * x + y * y).toDouble()).toFloat()
    x /= length
    y /= length
    return Vector2(x * speed, y * speed)
  }

  fun roundedFloat(num: Float, decimals: Int): Float {
    val scale = 10.0.pow(decimals.toDouble()).toFloat()
    return Math.round(num * scale) / scale
  }

  fun roundedVector2(vector2: Vector2, decimals: Int) {
    vector2.x = roundedFloat(vector2.x, decimals)
    vector2.y = roundedFloat(vector2.y, decimals)
  }

  fun rectToBBox(rectangle: Rectangle): BoundingBox {
    return BoundingBox(
        Vector3(rectangle.getX(), rectangle.getY(), 0.0f),
        Vector3(
            rectangle.getX() + rectangle.getWidth(),
            rectangle.getY() + rectangle.getHeight(),
            0.0f))
  }

  fun interpolate(start: Vector2, target: Vector2, delta: Float): Vector2 {
    val interPos = Vector2()
    interPos.x = interpolate(start.x, target.x, delta)
    interPos.y = interpolate(start.y, target.y, delta)
    return interPos
  }

  fun interpolate(start: Float, target: Float, delta: Float): Float {
    return start - (start - target) * delta
  }

  fun getOverlapPushDirection(
      toBePushed: Rectangle,
      other: Rectangle,
      overlap: Rectangle
  ): Direction? {
    Intersector.intersectRectangles(toBePushed, other, overlap)
    if (overlap.width == 0f && overlap.height == 0f) {
      return null
    }
    return if (overlap.width > overlap.height) {
      if (toBePushed.y > other.y) Direction.UP else Direction.DOWN
    } else {
      if (toBePushed.x > other.x) Direction.RIGHT else Direction.LEFT
    }
  }
}
