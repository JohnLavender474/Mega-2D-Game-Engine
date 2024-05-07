package com.engine.common.shapes

import com.badlogic.gdx.math.Circle

/**
 * Gets the bounding rectangle of the circle.
 *
 * @return The bounding rectangle of the circle.
 */
fun Circle.getBoundingRectangle() = GameRectangle(x - radius, y - radius, radius * 2f, radius * 2f)

/**
 * Converts this [Circle] to a [GameCircle].
 *
 * @return A [GameCircle] with the same fields as this [Circle].
 */
fun Circle.toGameCircle() = GameCircle(this)
