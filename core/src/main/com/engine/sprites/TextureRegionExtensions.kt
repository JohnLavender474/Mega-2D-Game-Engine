package com.engine.sprites

import com.badlogic.gdx.graphics.g2d.TextureRegion

fun TextureRegion.splitAndFlatten(rows: Int, columns: Int): Array<TextureRegion> {
  val splitRegions = split(regionWidth / columns, regionHeight / rows)
  return Array(rows * columns) {
    val row = it.floorDiv(columns)
    val column = it % columns
    splitRegions[row][column]
  }
}
