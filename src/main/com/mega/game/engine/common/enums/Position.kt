package com.mega.game.engine.common.enums

/** Represents a position on a 3x3 grid. */
enum class Position(val x: Int, val y: Int) {
    TOP_LEFT(0, 2),
    TOP_CENTER(1, 2),
    TOP_RIGHT(2, 2),
    CENTER_LEFT(0, 1),
    CENTER(1, 1),
    CENTER_RIGHT(2, 1),
    BOTTOM_LEFT(0, 0),
    BOTTOM_CENTER(1, 0),
    BOTTOM_RIGHT(2, 0);

    companion object {

        /**
         * Returns the [Position] at the given [x] and [y] coordinates.
         *
         * @param x the x coordinate
         * @param y the y coordinate
         * @return the [Position] at the given [x] and [y] coordinates
         */
        fun get(x: Int, y: Int): Position? = Position.values().find { it.x == x && it.y == y }
    }

    fun opposite(): Position {
        val newX = when (x) {
            0 -> 2
            1 -> 1
            2 -> 0
            else -> throw IllegalArgumentException("Invalid x value: $x")
        }
        val newY = when (y) {
            0 -> 2
            1 -> 1
            2 -> 0
            else -> throw IllegalArgumentException("Invalid y value: $y")
        }
        return get(newX, newY)!!
    }
}
