package com.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.toBoundingBox

/**
 * Checks if this [Camera] overlaps the given [Rectangle].
 *
 * @param bounds the [Rectangle] to check for overlap
 * @return true if this [Camera] overlaps the given [Rectangle], false otherwise
 */
fun Camera.overlaps(bounds: Rectangle) = frustum.boundsInFrustum(bounds.toBoundingBox())
