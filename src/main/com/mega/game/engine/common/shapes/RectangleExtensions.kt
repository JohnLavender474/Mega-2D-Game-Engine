package com.mega.game.engine.common.shapes

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.mega.game.engine.common.extensions.isInCamera
import com.mega.game.engine.common.getRandom


fun Rectangle.getRandomPositionInBounds(): Vector2 {
    val randX = getRandom(x, x + width)
    val randY = getRandom(y, y + height)
    return Vector2(randX, randY)
}


fun Rectangle.toBoundingBox() = BoundingBox(Vector3(x, y, 0f), Vector3(x + width, y + height, 0f))


fun Rectangle.isInCamera(camera: Camera) = toBoundingBox().isInCamera(camera)


fun Rectangle.toIntArray() =
    intArrayOf(x.toInt(), y.toInt(), (width + 1).toInt(), (height + 1).toInt())


fun Rectangle.toGameRectangle() = GameRectangle(this)


fun Rectangle.getPosition() = Vector2(x, y)


fun Rectangle.getCenter(): Vector2 = getCenter(Vector2())
