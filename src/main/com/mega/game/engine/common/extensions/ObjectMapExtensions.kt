package com.mega.game.engine.common.extensions

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.objects.GamePair

/**
 * Creates an [ObjectMap] from the given [GamePair]s.
 *
 * @param pairs the [GamePair]s to add to the [ObjectMap]
 * @return the [ObjectMap] created from the given [GamePair]s
 */
fun <T, U> objectMapOf(vararg pairs: GamePair<T, U>): ObjectMap<T, U> {
    val map = ObjectMap<T, U>()
    pairs.forEach { map.put(it.first, it.second) }
    return map
}

/**
 * Creates an [OrderedMap] from the given [GamePair]s.
 *
 * @param pairs the [GamePair]s to add to the [OrderedMap]
 * @return the [OrderedMap] created from the given [GamePair]s
 */
fun <T, U> orderedMapOf(vararg pairs: GamePair<T, U>): OrderedMap<T, U> {
    val map = OrderedMap<T, U>()
    pairs.forEach { map.put(it.first, it.second) }
    return map
}

/**
 * Put the specified default value in the map if the key does not already have a value, then return the value mapped to
 * the key (the specified value if no value was mapped to the key beforehand).
 *
 * @param key the key whose value is to be computed
 * @param defaultValue the default mapping of the key
 * @return the value associated with the key (either the existing value or the specified default value)
 */
fun <K, V> ObjectMap<K, V>.putIfAbsentAndGet(key: K, defaultValue: V): V {
    if (!containsKey(key)) put(key, defaultValue)
    return get(key)
}
