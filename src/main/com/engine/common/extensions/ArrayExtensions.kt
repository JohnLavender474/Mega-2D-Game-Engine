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
 * Creates an [Array] of the specified [size] with the given [value].
 *
 * @param size the size of the array
 * @return the [Array] created with the specified [size] and [value]
 */
fun <T> gdxFilledArrayOf(size: Int, value: T): Array<T> {
    val array = gdxArrayOf<T>()
    for (i in 0 until size) array.add(value)
    return array
}

/**
 * Fills the elements of the array with the specified [value] from 0 to [Array.size].
 *
 * @param value The value to fill the array with.
 * @return The modified array with all elements set to [value].
 */
fun <T> Array<T>.fill(value: T): Array<T> {
    for (i in 0 until size) set(i, value)
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

/**
 * Processes and filters the elements of the array based on the given [process] and [filter] functions.
 * The [process] function is applied to each element, and the [filter] function is used to determine
 * which elements to include in the new array. The [process] function is applied before the [filter]
 * function. The order of the elements is preserved.
 *
 * @param process The processing function to apply to each element.
 * @param filter The filter condition to apply to each element.
 * @return A new array containing only the elements that satisfy the [filter] condition.
 */
fun <T> Array<T>.processAndFilter(process: (T) -> Unit, filter: (T) -> Boolean): Array<T> {
    val array = Array<T>()
    forEach {
        process(it)
        if (filter(it)) array.add(it)
    }
    return array
}

/**
 * Gets the specified amount of random elements from the array. Each element is from a unique index.
 *
 * @param amount the amount of random elements to fetch
 * @return an array of random elements from unique indices
 */
fun <T> Array<T>.getRandomElements(amount: Int): Array<T> {
    require(amount >= 0) { "Number of indices must not be negative." }
    require(amount <= size) { "Number of indices must not exceed the size of the array." }

    if (amount == 0) return Array()

    val copy = Array(this)
    copy.shuffle()
    copy.truncate(amount)

    return copy
}
