package com.mega.game.engine.common.objects

import com.badlogic.gdx.utils.ObjectMap
import java.util.function.BiFunction

/**
 * Compute a new value for each key in the map.
 *
 * @param function The function to compute the new value.
 */
fun <K, V> ObjectMap<K, V>.computeValues(function: (K, V) -> V) {
    val keys = keys().toArray()
    keys.forEach { key -> put(key, function(key, get(key))) }
}

/**
 * Convenience method for using a [BiFunction] to compute new values.
 *
 * @param function The function to compute the new value.
 */
fun <K, V> ObjectMap<K, V>.computeValues(function: BiFunction<K, V, V>) =
    computeValues { key, value -> function.apply(key, value) }