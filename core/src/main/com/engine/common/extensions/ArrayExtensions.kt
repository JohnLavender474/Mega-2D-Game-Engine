package com.engine.common.extensions

import com.badlogic.gdx.utils.Array

fun <T> gdxArrayOf(vararg elements: T): Array<T> {
  val array = Array<T>()
  elements.forEach { array.add(it) }
  return array
}

fun <T> Array<T>.fill(value: T): Array<T> {
  for (i in 0 until size) {
    set(i, value)
  }
  return this
}

fun <T> Array<T>.filter(predicate: (T) -> Boolean): Array<T> {
  val array = Array<T>()
  forEach { if (predicate(it)) array.add(it) }
  return array
}

fun <T, R> Array<T>.map(transform: (T) -> R): Array<R> {
  val array = Array<R>()
  forEach { array.add(transform(it)) }
  return array
}
