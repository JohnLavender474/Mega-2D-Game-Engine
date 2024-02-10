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
