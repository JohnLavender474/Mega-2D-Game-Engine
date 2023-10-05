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
