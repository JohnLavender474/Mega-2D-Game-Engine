package com.engine.common.extensions

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

/**
 * Converts this [Vector3] to a [Vector2] by dropping the z value.
 *
 * @return the new [Vector2]
 */
fun Vector3.toVector2() = Vector2(x, y)
