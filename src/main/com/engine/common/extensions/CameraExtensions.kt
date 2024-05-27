package com.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.toBoundingBox

/**
 * Checks if this [Camera] overlaps the given [Rectangle].
 *
 * @param bounds the [Rectangle] to check for overlap
 * @return true if this [Camera] overlaps the given [Rectangle], false otherwise
 */
fun Camera.overlaps(bounds: Rectangle) = frustum.boundsInFrustum(bounds.toBoundingBox())

/**
 * Converts this [Camera] to a [GameRectangle]. The size is set to the [Camera.viewportWidth] and [Camera.viewportHeight]
 * and the center is set to the [Camera.position].
 *
 * @return a [GameRectangle] representing this [Camera]
 */
fun Camera.toGameRectangle(): GameRectangle {
    val rectangle = GameRectangle()
    rectangle.setSize(viewportWidth, viewportHeight)
    rectangle.setCenter(position.x, position.y)
    return rectangle
}

