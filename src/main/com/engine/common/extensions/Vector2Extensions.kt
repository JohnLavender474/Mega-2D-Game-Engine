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
