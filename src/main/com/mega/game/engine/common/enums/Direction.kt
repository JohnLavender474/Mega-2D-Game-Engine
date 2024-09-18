package com.mega.game.engine.common.enums

/** Represents a cardinal direction.
 *
 * @param rotation The rotation of the direction in degrees.
 */
enum class Direction(val rotation: Float) {

    UP(0f), DOWN(180f), LEFT(90f), RIGHT(270f);

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
}
