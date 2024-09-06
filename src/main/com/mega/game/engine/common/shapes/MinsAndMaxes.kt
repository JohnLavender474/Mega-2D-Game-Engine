package com.mega.game.engine.common.shapes

import kotlin.math.ceil
import kotlin.math.floor

/**
 * Defines the mins and maxes of a shape.
 * 
 * @property minX the minimum x
 * @property minY the minimum y
 * @property maxX the maximum x
 * @property maxY the maximum y
 */
data class MinsAndMaxes(var minX: Int, var minY: Int, var maxX: Int, var maxY: Int) {

    companion object {

        /**
         * Creates a new [MinsAndMaxes] object from the specified [bounds]. For the [minX] and [minY] values, the x and
         * y values of the bounds are rounded down and converted to integers. For the [maxX] and [maxY] values, the max
         * x and max y values are rounded up and converted to integers.
         *
         * @param bounds the bounds to use
         * @return the new [MinsAndMaxes] object
         */
        fun of(bounds: GameRectangle): MinsAndMaxes = MinsAndMaxes(
            floor(bounds.x).toInt(), floor(bounds.y).toInt(),
            ceil(bounds.getMaxX()).toInt(), ceil(bounds.getMaxY()).toInt()
        )
    }
}