package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Circle

/**
 * Gets the bounding rectangle of the circle. If bounds are supplied, then it is used instead of a new instance.
 *
 * @param bounds the optional bounds
 * @return The bounding rectangle of the circle.
 */
fun Circle.getBoundingRectangle(bounds: GameRectangle? = null): GameRectangle {
    val circleBounds = bounds ?: GameRectangle()
    return circleBounds.set(x - radius, y - radius, radius * 2f, radius * 2f)
}

/**
 * Converts this [Circle] to a [GameCircle].
 *
 * @return A [GameCircle] with the same fields as this [Circle].
 */
fun Circle.toGameCircle() = GameCircle(this)
