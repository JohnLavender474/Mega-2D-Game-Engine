package com.mega.game.engine.common.shapes

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.mega.game.engine.common.extensions.isInCamera
import com.mega.game.engine.common.getRandom

/**
 * Returns a random position contained within this [Rectangle].
 *
 * @return the random position
 */
fun Rectangle.getRandomPositionInBounds(): Vector2 {
    val randX = getRandom(x, x + width)
    val randY = getRandom(y, y + height)
    return Vector2(randX, randY)
}

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

/**
 * Returns the position of the [Rectangle] as a [Vector2].
 *
 * @return The position of the rectangle as a vector.
 */
fun Rectangle.getPosition() = Vector2(x, y)

/**
 * Returns the center of the [Rectangle] as a [Vector2].
 *
 * @return The center of the rectangle as a vector.
 */
fun Rectangle.getCenter(): Vector2 = getCenter(Vector2())
