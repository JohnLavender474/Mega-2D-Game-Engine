package com.mega.game.engine.common.shapes

import kotlin.math.ceil
import kotlin.math.floor


data class MinsAndMaxes(var minX: Int, var minY: Int, var maxX: Int, var maxY: Int) {

    companion object {


        fun of(bounds: GameRectangle): MinsAndMaxes = MinsAndMaxes(
            floor(bounds.x).toInt(), floor(bounds.y).toInt(),
            ceil(bounds.getMaxX()).toInt(), ceil(bounds.getMaxY()).toInt()
        )
    }
}