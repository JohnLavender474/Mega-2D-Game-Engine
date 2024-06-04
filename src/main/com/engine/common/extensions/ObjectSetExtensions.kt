package com.engine.common.extensions

import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedSet

/**
 * Creates an [ObjectSet] from the given elements.
 *
 * @param elements the elements to add to the [ObjectSet]
 * @return the [ObjectSet] created from the given elements
 */
fun <T> objectSetOf(vararg elements: T): ObjectSet<T> {
    val set = ObjectSet<T>()
    elements.forEach { set.add(it) }
    return set
}

/**
 * Creates an [OrderedSet] from the given elements.
 *
 * @param elements the elements to add to the [OrderedSet]
 * @return the [OrderedSet] created from the given elements
 */
fun <T> orderedSetOf(vararg elements: T): OrderedSet<T> {
    val set = OrderedSet<T>()
    elements.forEach { set.add(it) }
    return set
}

/**
 * Adds the given [element] to the [ObjectSet] and returns the element.
 *
 * @param element the element to add
 * @return the element that was added
 */
fun <T> ObjectSet<T>.addAndReturn(element: T): T {
    add(element)
    return element
}

/**
 * Adds all the elements from the given [iterable] to the [ObjectSet] and returns the set.
 *
 * @param iterable the elements to add
 * @return the set with all the elements added
 */
fun <T> ObjectSet<T>.addAllAndReturn(iterable: Iterable<T>): ObjectSet<T> {
    iterable.forEach { add(it) }
    return this
}

/**
 * Adds all the elements from the given [elements] to the [ObjectSet] and returns the set.
 *
 * @param elements the elements to add
 * @return the set with all the elements added
 */
fun <T> ObjectSet<T>.addAllAndReturn(vararg elements: T): ObjectSet<T> {
    elements.forEach { add(it) }
    return this
}
