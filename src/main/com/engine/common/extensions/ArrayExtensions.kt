package com.engine.common.extensions

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.FloatArray
import com.badlogic.gdx.utils.IntArray
import com.badlogic.gdx.utils.Predicate
import java.util.function.Consumer
import java.util.function.Function

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
 * Convenience method that accepts a [Predicate] to filter the elements of the array.
 *
 * @param predicate The filter condition to apply to each element.
 * @return A new array containing only the elements that satisfy the [predicate].
 * @see filter
 */
fun <T> Array<T>.filter(predicate: Predicate<T>) = filter(predicate::evaluate)

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
 * Convenience method that accepts a [Function] to transform the elements of the array.
 *
 * @param transform The transformation function to apply to each element.
 * @return A new array containing the transformed elements.
 * @see map
 */
fun <T, R> Array<T>.map(transform: Function<T, R>) = map(transform::apply)

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
 * Convenience method that accepts a [Consumer] and [Predicate] to process and filter the elements of the array.
 *
 * @param process The processing function to apply to each element.
 * @param filter The filter condition to apply to each element.
 * @return A new array containing only the elements that satisfy the [filter] condition.
 * @see processAndFilter
 */
fun <T> Array<T>.processAndFilter(process: Consumer<T>, filter: Predicate<T>) =
    processAndFilter(process::accept, filter::evaluate)

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

/**
 * Creates a [FloatArray] from the given elements.
 *
 * @param elements the elements to add to the [FloatArray]
 * @return the [FloatArray] created from the given elements
 */
fun gdxFloatArrayOf(vararg elements: Float): FloatArray {
    val array = FloatArray()
    elements.forEach { array.add(it) }
    return array
}

/**
 * Creates an [IntArray] from the given elements.
 *
 * @param elements the elements to add to the [IntArray]
 * @return the [IntArray] created from the given elements
 */
fun gdxIntArrayOf(vararg elements: Int): IntArray {
    val array = IntArray()
    elements.forEach { array.add(it) }
    return array
}

/**
 * Adds the given [element] to the array and returns the array.
 *
 * @param element the element to add
 * @return the array with the element added
 */
fun <T> Array<T>.addAndReturn(element: T): Array<T> {
    add(element)
    return this
}

/**
 * Adds all the elements from the given [iterable] to the array and returns the array.
 * This method is useful for chaining multiple operations together.
 *
 * @param iterable the iterable to add elements from
 * @return the array with all the elements from the iterable added
 */
fun <T> Array<T>.addAllAndReturn(iterable: Iterable<T>): Array<T> {
    iterable.forEach { add(it) }
    return this
}

/**
 * Adds all the elements from the given [elements] to the array and returns the array.
 * This method is useful for chaining multiple operations together.
 *
 * @param elements the elements to add to the array
 * @return the array with all the elements added
 */
fun <T> Array<T>.addAllAndReturn(vararg elements: T): Array<T> {
    elements.forEach { add(it) }
    return this
}
