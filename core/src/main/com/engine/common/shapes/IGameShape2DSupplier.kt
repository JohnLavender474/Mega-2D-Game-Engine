package com.engine.common.shapes

/** A supplier of a [IGameShape2D]. */
fun interface IGameShape2DSupplier {

  /**
   * Gets the [IGameShape2D] from this supplier.
   *
   * @return the [IGameShape2D]
   */
  fun getGameShape2D(): IGameShape2D
}
