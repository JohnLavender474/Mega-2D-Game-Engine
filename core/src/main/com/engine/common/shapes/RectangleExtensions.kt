package com.engine.common.shapes

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.engine.common.extensions.isInCamera

/**
 * Converts a [Rectangle] to a [BoundingBox] in 3D space with the z-coordinate set to 0.
 *
 * @return A 3D bounding box representation of the rectangle.
 */
fun Rectangle.toBoundingBox() = BoundingBox(Vector3(x, y, 0f), Vector3(x + width, y + height, 0f))

/**
 * Checks if the [Rectangle] is within the specified camera's view.
 *
 * @param camera The camera to check against.
 * @return `true` if the rectangle is within the camera's view; otherwise, `false`.
 */
fun Rectangle.isInCamera(camera: Camera) = toBoundingBox().isInCamera(camera)

/**
 * Converts a [Rectangle] to an array of integers containing its x, y, width, and height values.
 *
 * @return An integer array representing the rectangle's properties.
 */
fun Rectangle.toIntArray() =
    intArrayOf(x.toInt(), y.toInt(), (width + 1).toInt(), (height + 1).toInt())

/**
 * Converts a [Rectangle] to a [GameRectangle].
 *
 * @return A [GameRectangle] representing the same region as the rectangle.
 */
fun Rectangle.toGameRectangle() = GameRectangle(this)
