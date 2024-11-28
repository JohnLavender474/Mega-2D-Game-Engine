package com.mega.game.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.common.shapes.toBoundingBox


fun Camera.overlaps(bounds: Rectangle) = frustum.boundsInFrustum(bounds.toBoundingBox())


fun Camera.toGameRectangle(rectangle: GameRectangle = GameRectangle()): GameRectangle {
    rectangle.setSize(viewportWidth, viewportHeight)
    rectangle.setCenter(position.x, position.y)
    return rectangle
}

