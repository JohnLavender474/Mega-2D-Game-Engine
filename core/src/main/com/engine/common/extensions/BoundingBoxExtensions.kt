package com.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.collision.BoundingBox

/**
 * Returns `true` if this [BoundingBox] is in the [Camera]'s frustum.
 *
 * @param camera the [Camera] to check
 * @return whether this [BoundingBox] is in the [Camera]'s frustum
 */
fun BoundingBox.isInCamera(camera: Camera) = camera.frustum.boundsInFrustum(this)
