package com.engine.common.shapes

/** A supplier of a [GameShape2D]. */
fun interface GameShape2DSupplier {

  /**
   * Gets the [GameShape2D] from this supplier.
   *
   * @return the [GameShape2D]
   */
  fun getGameShape2D(): GameShape2D
}
