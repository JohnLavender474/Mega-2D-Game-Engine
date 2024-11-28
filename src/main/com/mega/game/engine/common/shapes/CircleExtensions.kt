package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Circle


fun Circle.getBoundingRectangle(bounds: GameRectangle? = null): GameRectangle {
    val circleBounds = bounds ?: GameRectangle()
    return circleBounds.set(x - radius, y - radius, radius * 2f, radius * 2f)
}


fun Circle.toGameCircle() = GameCircle(this)
