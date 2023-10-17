package com.engine.common.extensions

import com.badlogic.gdx.utils.Array

/**
 * Creates an [Array] from the given elements.
 *
 * @param elements the elements to add to the [Array]
 * @return the [Array] created from the given elements
 */
fun <T> gdxArrayOf(vararg elements: T): Array<T> {
  val array = Array<T>()
  elements.forEach { array.add(it) }
  return array
}

/**
 * Fills the elements of the array with the specified [value].
 *
 * @param value The value to fill the array with.
 * @return The modified array with all elements set to [value].
 */
fun <T> Array<T>.fill(value: T): Array<T> {
  for (i in 0 until size) {
    set(i, value)
  }
  return this
}

/**
 * Filters the elements of the array based on the given [predicate].
 *
 * @param predicate The filter condition to apply to each element.
 * @return A new array containing only the elements that satisfy the [predicate].
 */
fun <T> Array<T>.filter(predicate: (T) -> Boolean): Array<T> {
  val array = Array<T>()
  forEach { if (predicate(it)) array.add(it) }
  return array
}

/**
 * Transforms the elements of the array using the provided [transform] function.
 *
 * @param transform The transformation function to apply to each element.
 * @return A new array containing the transformed elements.
 */
fun <T, R> Array<T>.map(transform: (T) -> R): Array<R> {
  val array = Array<R>()
  forEach { array.add(transform(it)) }
  return array
}
