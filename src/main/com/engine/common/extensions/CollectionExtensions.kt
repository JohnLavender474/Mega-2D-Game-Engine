package com.engine.common.extensions

import com.badlogic.gdx.utils.Array
import com.engine.common.objects.ImmutableCollection
import java.util.function.Predicate

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

/**
 * Convenience method that accepts a [Predicate] to filter the elements of the collection.
 *
 * @param predicate The filter condition to apply to each element.
 * @return A new array containing only the elements that satisfy the [predicate].
 * @param R the type to filter by
 * @return a [List] of all elements in this [Collection] that are of type [R]
 * @see Predicate
 */
inline fun <reified R> Collection<*>.filterTyle(predicate: Predicate<R>) = filterType<R> { predicate.test(it) }

/**
 * Converts this [Collection] to a [Array].
 */
fun <T> Collection<T>.toGdxArray(): Array<T> {
    val array = Array<T>()
    forEach { array.add(it) }
    return array
}
