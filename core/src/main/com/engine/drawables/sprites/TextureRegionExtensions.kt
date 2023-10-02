package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

/**
 * Splits this texture region into a 2D array of texture regions, then flattens the 2D array to one
 * dimension. The texture regions are ordered from left to right, top to bottom. The number of rows
 * and columns must be greater than 0.
 *
 * @param rows the number of rows
 * @param columns the number of columns
 * @return the flattened array of texture regions
 */
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
