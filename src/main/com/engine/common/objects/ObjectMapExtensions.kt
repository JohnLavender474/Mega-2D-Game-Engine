package com.engine.common.objects

import com.badlogic.gdx.utils.ObjectMap

/**
 * Compute a new value for each key in the map.
 *
 * @param function The function to compute the new value.
 */
fun <K, V> ObjectMap<K, V>.computeValues(function: (K, V) -> V) {
    val keys = keys().toArray()
    keys.forEach { key -> put(key, function(key, get(key))) }
}