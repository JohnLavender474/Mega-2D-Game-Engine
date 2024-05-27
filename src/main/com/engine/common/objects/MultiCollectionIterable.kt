package com.engine.common.objects

import com.badlogic.gdx.utils.Array
import java.util.function.Consumer

/**
 * Iterator for iterating over multiple collections in sequence.
 *
 * @param multiCollectionIterable A MultiCollectionIterable containing the collections to iterate over.
 */
class MultiCollectionIterator<T>(private val multiCollectionIterable: MultiCollectionIterable<T>) : Iterator<T> {

    private var outerIndex = 0
    private var currentInnerIterator: Iterator<T>? = null

    /**
     * Checks if there are more elements to iterate.
     *
     * @return True if there are more elements to iterate, false otherwise.
     */
    override fun hasNext(): Boolean {
        val iterables = multiCollectionIterable.iterables
        if (outerIndex >= iterables.size) return false

        if (currentInnerIterator == null) {
            // Initialize the inner iterator with the current collection
            currentInnerIterator = iterables[outerIndex].iterator()
        }

        while (!currentInnerIterator!!.hasNext()) {
            // Move to the next collection when the current one is exhausted
            outerIndex++
            if (outerIndex >= iterables.size) return false
            currentInnerIterator = iterables[outerIndex].iterator()
        }

        return true
    }

    /**
     * Retrieves the next element in the sequence.
     *
     * @return The next element in the sequence.
     * @throws NoSuchElementException if there are no more elements to iterate.
     */
    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        return currentInnerIterator!!.next()
    }
}

/**
 * Iterable for creating a MultiCollectionIterator to iterate over multiple collections.
 *
 * @param iterables An array of Iterables to iterate over.
 */
class MultiCollectionIterable<T>(internal val iterables: Array<Iterable<T>>) : Iterable<T> {

    /**
     * Returns a MultiCollectionIterator to iterate over the collections.
     *
     * @return A MultiCollectionIterator instance.
     */
    override fun iterator() = MultiCollectionIterator(this)

    /**
     * Iterates over the collections and executes the given action for each element. The action receives the outer index,
     * inner index, and the value of the element. The outer index is the index of the value in the entire sequence of
     * elements, and the inner index is the index of the value in the current collection.
     *
     * @param action The action to execute for each element.
     */
    fun forEach(action: (outerIndex: Int, innerIndex: Int, value: T) -> Unit) {
        var outerIndex = 0
        iterables.forEach {
            var innerIndex = 0
            it.forEach { value ->
                action(outerIndex, innerIndex, value)
                innerIndex++
                outerIndex++
            }
        }
    }

    /**
     * Convenience method for Java interop. Iterates over the collections and executes the given action for each element.
     *
     * @param action The action to execute for each element.
     */
    fun forEach(action: Consumer<Triple<Int, Int, T>>) =
        forEach { outerIndex, innerIndex, value -> action.accept(Triple(outerIndex, innerIndex, value)) }
}
