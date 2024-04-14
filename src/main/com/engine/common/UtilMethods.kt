package com.engine.common

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.engine.common.enums.Direction
import com.engine.common.shapes.IGameShape2D
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A [Random] instance with the seed being [System.currentTimeMillis].
 */
val random = Random(System.currentTimeMillis())

/**
 * Generates a random boolean value.
 *
 * @return A random boolean value.
 */
fun getRandomBool() = getRandom(0, 1) == 1

/**
 * Generates a random integer within the specified range.
 *
 * @param min The minimum value (inclusive).
 * @param max The maximum value (inclusive).
 * @return A random integer between min and max (inclusive).
 */
fun getRandom(min: Int, max: Int): Int {
    return random.nextInt(max + 1 - min) + min
}

/**
 * Generates a random float within the specified range.
 *
 * @param min The minimum value (inclusive).
 * @param max The maximum value (inclusive).
 * @return A random float between min and max (inclusive).
 */
fun getRandom(min: Float, max: Float): Float {
    return random.nextFloat() * (max - min) + min
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
 * Determines the single most direction from the [other] shape to the [toBePushed] shape based on their bounds. In other
 * words, this method determines in which direction the [toBePushed] shape should be pushed to resolve the overlap with
 * the [other] shape based on the overlap of their bounding rectangles via [IGameShape2D.getBoundingRectangle].
 *
 * @param toBePushed The shape to be pushed.
 * @param other The shape to be pushed against.
 * @param overlap The rectangle to store the overlap in. Optional.
 * @return The direction from start to target, or null if the two rectangles do not overlap.
 */
fun getOverlapPushDirection(
    toBePushed: IGameShape2D, other: IGameShape2D, overlap: Rectangle = Rectangle()
): Direction? {
    val toBePushedBounds = toBePushed.getBoundingRectangle()
    val otherBounds = other.getBoundingRectangle()
    return if (Intersector.intersectRectangles(toBePushedBounds, otherBounds, overlap)) {
        if (overlap.width > overlap.height) {
            if (toBePushedBounds.y > otherBounds.y) Direction.UP else Direction.DOWN
        } else if (toBePushedBounds.x > otherBounds.x) Direction.RIGHT else Direction.LEFT
    } else null
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
fun roundFloat(num: Float, decimals: Int): Float {
    val scale = 10.0.pow(decimals.toDouble()).toFloat()
    return Math.round(num * scale) / scale
}

/**
 * Rounds the components of a Vector2 to a specified number of decimal places.
 *
 * @param vector2 The Vector2 to be rounded.
 * @param decimals The number of decimal places to round to.
 */
fun roundVector2(vector2: Vector2, decimals: Int) {
    vector2.x = roundFloat(vector2.x, decimals)
    vector2.y = roundFloat(vector2.y, decimals)
}

/**
 * Converts a [Rectangle] to a [BoundingBox] in 3D space with the z-coordinate set to 0.
 *
 * @param rectangle The rectangle to be converted.
 * @return A 3D bounding box representation of the rectangle.
 */
fun rectToBBox(rectangle: Rectangle): BoundingBox {
    return BoundingBox(
        Vector3(rectangle.getX(), rectangle.getY(), 0.0f), Vector3(
            rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight(), 0.0f
        )
    )
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

/**
 * Calculates the angle in degrees between two vectors.
 *
 * @param origin The origin vector.
 * @param target The target vector.
 * @return The angle in degrees between the two vectors.
 */
fun calculateAngleDegrees(origin: Vector2, target: Vector2): Float {
    val deltaX = target.x - origin.x
    val deltaY = target.y - origin.y
    val angleRadians = atan2(deltaY.toDouble(), deltaX.toDouble())
    return Math.toDegrees(angleRadians).toFloat()
}
