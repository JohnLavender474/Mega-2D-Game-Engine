package com.mega.game.engine.common.enums

import com.mega.game.engine.common.enums.Direction.values


/** Represents a cardinal direction.
 *
 * @param rotation The rotation of the direction in degrees.
 */
enum class Direction(val rotation: Float) {

    UP(0f), LEFT(90f), DOWN(180f), RIGHT(270f);

    /**
     * Returns the opposite direction of this one.
     *
     * @return the opposite direction
     */
    fun getOpposite() = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }

    /**
     * Returns true if this direction is horizontal.
     *
     * @return True if this direction is horizontal.
     */
    fun isHorizontal() = this == LEFT || this == RIGHT

    /**
     * Returns true if this direction is vertical.
     *
     * @return True if this direction is vertical.
     */
    fun isVertical() = this == UP || this == DOWN

    /**
     * Gets the [Direction] value rotated clockwise from this value.
     *
     * @return the value rotated clockwise
     */
    fun getRotatedClockwise(): Direction {
        var index = ordinal - 1
        if (index < 0) index = values().size - 1
        return values()[index]
    }

    /**
     * Gets the [Direction] value rotated counter-clockwise from this value.
     *
     * @return the value rotated counter-clockwise
     */
    fun getRotatedCounterClockwise(): Direction {
        var index = ordinal + 1
        if (index == values().size) index = 0
        return values()[index]
    }
}
