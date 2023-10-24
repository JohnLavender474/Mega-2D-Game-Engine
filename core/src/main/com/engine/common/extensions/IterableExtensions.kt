package com.engine.common.extensions

import com.badlogic.gdx.utils.Array

fun <T> Iterable<T>.toGdxArray(): Array<T> {
  val array = Array<T>()
  this.forEach { array.add(it) }
  return array
}
