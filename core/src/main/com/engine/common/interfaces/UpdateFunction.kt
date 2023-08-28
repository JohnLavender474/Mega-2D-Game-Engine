package com.engine.common.interfaces

/** A function that updates an object. */
fun interface UpdateFunction<T, R> {

  /** Applies this function to the given data. */
  fun apply(data: T, delta: Float): R
}
