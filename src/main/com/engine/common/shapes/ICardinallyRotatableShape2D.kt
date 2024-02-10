package com.engine.common.shapes

import com.engine.common.enums.Direction

/** A shape that can be rotated in 90 degree increments. */
interface ICardinallyRotatableShape2D {

    /**
     * Gets the cardinally rotated shape. This is the shape rotated by 90 degrees in the given
     * direction. It is up to the implementer to decide what degree values are considered "up",
     * "down", "left", and "right".
     *
     * @param direction The direction to rotate the shape.
     * @param useNewShape Whether to return a new shape or modify the current shape.
     * @return The cardinally rotated shape.
     */
    fun getCardinallyRotatedShape(direction: Direction, useNewShape: Boolean): IGameShape2D
}
