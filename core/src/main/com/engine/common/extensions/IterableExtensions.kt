package com.engine.common.extensions

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedSet

/**
 * Converts this [Iterable] to a [Array].
 *
 * @return a new [Array] containing all elements of this [Iterable].
 */
fun <T> Iterable<T>.toGdxArray(): Array<T> {
    val array = Array<T>()
    this.forEach { array.add(it) }
    return array
}

/**
 * Converts this [Iterable] to a [ObjectSet].
 *
 * @return a new [ObjectSet] containing all elements of this [Iterable].
 */
fun <T> Iterable<T>.toObjectSet(): ObjectSet<T> {
    val set = ObjectSet<T>()
    this.forEach { set.add(it) }
    return set
}

/**
 * Converts this [Iterable] to a [OrderedSet].
 *
 * @return a new [OrderedSet] containing all elements of this [Iterable].
 */
fun <T> Iterable<T>.toOrderedSet(): OrderedSet<T> {
    val set = OrderedSet<T>()
    this.forEach { set.add(it) }
    return set
}
