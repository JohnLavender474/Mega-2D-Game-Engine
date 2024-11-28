package com.mega.game.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array


fun TextureRegion.splitAndFlatten(rows: Int, columns: Int): Array<TextureRegion> {
    val splitRegions = split(regionWidth / columns, regionHeight / rows)
    val array = Array<TextureRegion>(true, rows * columns)
    val size = rows * columns
    for (i in 0 until size) {
        val row = i.floorDiv(columns)
        val column = i % columns
        array.add(splitRegions[row][column])
    }
    return array
}
