package com.engine.common.extensions

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap

/**
 * Creates an [ObjectMap] from the given [Pair]s.
 *
 * @param pairs the [Pair]s to add to the [ObjectMap]
 * @return the [ObjectMap] created from the given [Pair]s
 */
fun <T, U> objectMapOf(vararg pairs: Pair<T, U>): ObjectMap<T, U> {
    val map = ObjectMap<T, U>()
    pairs.forEach { map.put(it.first, it.second) }
    return map
}

/**
 * Creates an [OrderedMap] from the given [Pair]s.
 *
 * @param pairs the [Pair]s to add to the [OrderedMap]
 * @return the [OrderedMap] created from the given [Pair]s
 */
fun <T, U> orderedMapOf(vararg pairs: Pair<T, U>): OrderedMap<T, U> {
    val map = OrderedMap<T, U>()
    pairs.forEach { map.put(it.first, it.second) }
    return map
}

/**
 * Put the value in the map if the key does not already have a value. If the key does have a value
 * then the value is returned.
 *
 * @param key the key whose value is to be computed
 * @param defaultValue the default mapping of the key
 * @return the current (existing or computed) value associated with the specified key, or null if
 *   the computed value is null
 */
fun <K, V> ObjectMap<K, V>.putIfAbsentAndGet(key: K, defaultValue: V): V {
    if (!containsKey(key)) put(key, defaultValue)
    return get(key)
}
