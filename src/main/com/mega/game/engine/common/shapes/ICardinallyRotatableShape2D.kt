package com.mega.game.engine.common.shapes

import com.mega.game.engine.common.enums.Direction

/** A shape that can be rotated in 90 degree increments. */
interface ICardinallyRotatableShape2D: IGameShape2D {

    /**
     * Gets the cardinally rotated shape. This is the shape rotated by 90 degrees in the given direction. It is up to
     * the implementer to decide what degree values are considered "up", "down", "left", and "right".
     *
     * If [returnShape] is null, then the implementing class should decide what to do. If the [returnShape] is not
     * supplied, then it defaults to this, meaning this instance is modified. Also, in most cases the [returnShape]
     * will need to be of a specific type, therefore the child implementation may reject types.
     *
     * @param direction The direction to rotate the shape.
     * @param returnShape The return shape to use: can be null, this, or a new shape.
     * @return The cardinally rotated shape.
     */
    fun getCardinallyRotatedShape(direction: Direction, returnShape: IGameShape2D? = this): IGameShape2D
}
