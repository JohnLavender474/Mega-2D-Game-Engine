package com.engine.common.extensions

import com.engine.common.objects.Array2D

fun FloatArray.to2DArray(): Array2D<Float> {
  val array2D = Array2D<Float>(1, this.size)
  for (i in 0 until this.size) {
    array2D[0, i] = this[i]
  }
  return array2D
}
