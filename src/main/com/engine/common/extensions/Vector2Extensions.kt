package com.engine.common.extensions

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

/**
 * Converts this [Vector2] to a [Vector3] with the given z value.
 *
 * @param z the z value of the new [Vector3]
 * @return the new [Vector3]
 */
fun Vector2.toVector3(z: Float = 0f) = Vector3(x, y, z)

/**
 * Sets the x and y values of this [Vector2] to the given value.
 *
 * @param value the value to set the x and y values to
 * @return this [Vector2] for chaining
 */
fun Vector2.set(value: Float): Vector2 = set(value, value)

/**
 * Coerces the x value of this [Vector2] to be within the given range.
 *
 * @param min the minimum value
 * @param max the maximum value
 * @return this [Vector2] for chaining
 */
fun Vector2.coerceX(min: Float, max: Float): Vector2 {
    x = x.coerceIn(min, max)
    return this
}

/**
 * Coerces the y value of this [Vector2] to be within the given range.
 *
 * @param min the minimum value
 * @param max the maximum value
 * @return this [Vector2] for chaining
 */
fun Vector2.coerceY(min: Float, max: Float): Vector2 {
    y = y.coerceIn(min, max)
    return this
}

/**
 * Coerces the x and y values of this [Vector2] to be within the given range.
 *
 * @param min the minimum value
 * @param max the maximum value
 * @return this [Vector2] for chaining
 */
fun Vector2.coerce(min: Float, max: Float): Vector2 {
    x = x.coerceIn(min, max)
    y = y.coerceIn(min, max)
    return this
}

/**
 * Coerces the x and y values of this [Vector2] to be within the given range.
 *
 * @param min the minimum values
 * @param max the maximum values
 * @return this [Vector2] for chaining
 */
fun Vector2.coerce(min: Vector2, max: Vector2): Vector2 {
    x = x.coerceIn(min.x, max.x)
    y = y.coerceIn(min.y, max.y)
    return this
}

/**
 * Creates a [Vector2] where the x and y values are set to the given value.
 *
 * @param value the value to set the x and y values to
 * @return the new [Vector2]
 */
fun vector2Of(value: Float) = Vector2(value, value)
