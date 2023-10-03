package com.engine.common.extensions

import com.badlogic.gdx.utils.Array
import com.engine.common.objects.ImmutableCollection

/**
 * Converts this [Collection] to an [ImmutableCollection].
 *
 * @return an [ImmutableCollection] with the same elements as this [Collection]
 */
fun <T> Collection<T>.toImmutableCollection() = ImmutableCollection(this)

/**
 * Filters this [Collection] by the specified type.
 *
 * @param R the type to filter by
 * @return a [List] of all elements in this [Collection] that are of type [R]
 */
inline fun <reified R> Collection<*>.filterType(predicate: (R) -> Boolean): Array<R> {
  val array = Array<R>()
  forEach {
    if (it is R && predicate(it)) {
      array.add(it)
    }
  }
  return array
}

fun <T> Collection<T>.toGdxArray(): Array<T> {
  val array = Array<T>()
  forEach { array.add(it) }
  return array
}
