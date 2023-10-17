package com.engine.common

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

/**
 * Generates a random integer within the specified range.
 *
 * @param min The minimum value (inclusive).
 * @param max The maximum value (inclusive).
 * @return A random integer between min and max (inclusive).
 */
fun getRandom(min: Int, max: Int): Int {
  return Random(System.currentTimeMillis()).nextInt(max + 1 - min) + min
}

/**
 * Executes a consumer function if a random number matches any value in the provided iterable.
 *
 * @param min The minimum value (inclusive).
 * @param max The maximum value (inclusive).
 * @param matches The iterable of values to match.
 * @param runOnMatch The consumer function to run if there is a match.
 */
fun doIfRandomMatches(min: Int, max: Int, matches: Iterable<Int>, runOnMatch: Consumer<Int?>) {
  val r = getRandom(min, max)
  for (i in matches) {
    if (r == i) {
      runOnMatch.accept(r)
      break
    }
  }
}

/**
 * Checks if two objects match specified predicates.
 *
 * @param o1 The first object to compare.
 * @param o2 The second object to compare.
 * @param p1 The first predicate to apply.
 * @param p2 The second predicate to apply.
 * @return `true` if either predicate is satisfied for both objects, otherwise `false`.
 */
fun <T> mask(o1: T, o2: T, p1: Predicate<T>, p2: Predicate<T>): Boolean {
  return p1.test(o1) && p2.test(o2) || p2.test(o1) && p1.test(o2)
}

/**
 * Determines the single most direction from the start to the target based on their positions.
 *
 * @param start The starting rectangle.
 * @param target The target rectangle.
 * @return The direction from start to target.
 */
fun getSingleMostDirectionFromStartToTarget(start: Rectangle, target: Rectangle): Direction {
  val startCenter = start.getCenter(Vector2())
  val targetCenter = target.getCenter(Vector2())
  return getSingleMostDirectionFromStartToTarget(startCenter, targetCenter)
}

/**
 * Determines the single most direction from the start to the target based on their positions.
 *
 * @param start The starting vector.
 * @param target The target vector.
 * @return The direction from start to target.
 */
fun getSingleMostDirectionFromStartToTarget(start: Vector2, target: Vector2): Direction {
  val x = target.x - start.x
  val y = target.y - start.y
  return if (abs(x.toDouble()) > abs(y.toDouble())) {
    if (x > 0) Direction.RIGHT else Direction.LEFT
  } else {
    if (y > 0) Direction.UP else Direction.DOWN
  }
}

/**
 * Calculates the slope between two points.
 *
 * @param p1 The first point.
 * @param p2 The second point.
 * @return The slope between the two points.
 */
fun getSlope(p1: Vector2, p2: Vector2): Float {
  return (p1.y - p2.y) / (p1.x - p2.x)
}

/**
 * Normalizes a trajectory vector with a specified speed.
 *
 * @param start The start vector.
 * @param end The end vector.
 * @param speed The desired speed.
 * @return The normalized trajectory vector with the specified speed.
 */
fun normalizedTrajectory(start: Vector2, end: Vector2, speed: Float): Vector2 {
  var x = end.x - start.x
  var y = end.y - start.y
  val length = sqrt((x * x + y * y).toDouble()).toFloat()
  x /= length
  y /= length
  return Vector2(x * speed, y * speed)
}

/**
 * Rounds a floating-point number to a specified number of decimal places.
 *
 * @param num The number to be rounded.
 * @param decimals The number of decimal places to round to.
 * @return The rounded number.
 */
fun roundedFloat(num: Float, decimals: Int): Float {
  val scale = 10.0.pow(decimals.toDouble()).toFloat()
  return Math.round(num * scale) / scale
}

/**
 * Rounds the components of a Vector2 to a specified number of decimal places.
 *
 * @param vector2 The Vector2 to be rounded.
 * @param decimals The number of decimal places to round to.
 */
fun roundedVector2(vector2: Vector2, decimals: Int) {
  vector2.x = roundedFloat(vector2.x, decimals)
  vector2.y = roundedFloat(vector2.y, decimals)
}

/**
 * Converts a [Rectangle] to a [BoundingBox] in 3D space with the z-coordinate set to 0.
 *
 * @param rectangle The rectangle to be converted.
 * @return A 3D bounding box representation of the rectangle.
 */
fun rectToBBox(rectangle: Rectangle): BoundingBox {
  return BoundingBox(
      Vector3(rectangle.getX(), rectangle.getY(), 0.0f),
      Vector3(
          rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight(), 0.0f))
}

/**
 * Interpolates between two vectors based on a delta value.
 *
 * @param start The starting vector.
 * @param target The target vector.
 * @param delta The interpolation delta.
 * @return The interpolated vector.
 */
fun interpolate(start: Vector2, target: Vector2, delta: Float): Vector2 {
  val interPos = Vector2()
  interPos.x = interpolate(start.x, target.x, delta)
  interPos.y = interpolate(start.y, target.y, delta)
  return interPos
}

/**
 * Interpolates between two floats based on a delta value.
 *
 * @param start The starting float.
 * @param target The target float.
 * @param delta The interpolation delta.
 */
fun interpolate(start: Float, target: Float, delta: Float): Float {
  return start - (start - target) * delta
}
