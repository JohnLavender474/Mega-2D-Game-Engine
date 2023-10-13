package com.engine.common.extensions

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.engine.common.UtilMethods

fun Vector2.toVector3(z: Float = 0f) = Vector3(x, y, z)

fun Vector2.interpolate(target: Vector2, delta: Float) =
    UtilMethods.interpolate(this, target, delta)

