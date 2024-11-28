package com.mega.game.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.collision.BoundingBox
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.common.shapes.toBoundingBox

fun Camera.overlaps(bounds: Rectangle, out: BoundingBox) = frustum.boundsInFrustum(bounds.toBoundingBox(out))

fun Camera.toGameRectangle(out: GameRectangle): GameRectangle {
    out.setSize(viewportWidth, viewportHeight)
    out.setCenter(position.x, position.y)
    return out
}

