package com.mega.game.engine.common.objects

import com.badlogic.gdx.utils.Array

/**
 * Returns a new [MutableArray] containing the [values].
 *
 * @param values the var args of values
 * @return the new mutable array containing the values
 */
fun <T> mutableArrayOf(vararg values: T): MutableArray<T> {
    val array = MutableArray<T>()
    values.forEach { array.add(it) }
    return array
}

/**
 * A mutable collection backed by a LibGDX array. This collection does not extend the [Array] class. This collection
 * is not thread-safe.
 */
class MutableArray<T> : MutableCollection<T> {

    private val array = Array<T>()

    override val size = array.size

    /**
     * Sorts this array.
     * @see Array.sort
     */
    fun sort() = array.sort()

    /**
     * Sorts this array using the comparator.
     * @see Array.sort
     * @param comparator The comparator to use
     */
    fun sort(comparator: Comparator<T>) = array.sort(comparator)

    /**
     * Reverses this array.
     * @see Array.reverse
     */
    fun reverse() = array.reverse()

    /**
     * Shuffles this array.
     * @see Array.shuffle
     */
    fun shuffle() = array.shuffle()

    /**
     * Truncates this array to the new size.
     * @see Array.truncate
     */
    fun truncate(newSize: Int) = array.truncate(newSize)

    /**
     * Fetches a random element from this array.
     * @see Array.random
     * @return A random element from this array
     */
    fun random() = array.random()

    /**
     * Removes the element at the given index.
     * @see Array.removeIndex
     * @param index The index of the element to remove
     * @return The removed element
     */
    fun removeIndex(index: Int) = array.removeIndex(index)

    /**
     * Removes the elements in the given range.
     * @see Array.removeRange
     * @param start The start index of the range
     */
    fun removeRange(start: Int, end: Int) = array.removeRange(start, end)

    /**
     * Removes the first occurrence of the given value.
     * @see Array.removeValue
     * @param value The value to remove
     * @param identity If the removal should be done using identity
     * @return If the value was removed
     */
    fun removeValue(value: T, identity: Boolean) = array.removeValue(value, identity)

    /**
     * Pops the last element from this array.
     * @see Array.pop
     * @return The last element
     */
    fun pop() = array.pop()

    /**
     * Inserts the given value at the given index.
     * @see Array.insert
     * @param index The index to insert the value at
     * @param value The value to insert
     */
    fun insert(index: Int, value: T) = array.insert(index, value)

    /**
     * Swaps the elements at the given indices.
     * @see Array.swap
     * @param first The first index
     * @param second The second index
     */
    fun swap(first: Int, second: Int) = array.swap(first, second)

    /**
     * Sets the element at the given index.
     * @see Array.set
     * @param index The index to set the value at
     * @param value The value to set
     */
    fun set(index: Int, value: T) = array.set(index, value)

    /**
     * Gets the element at the given index.
     * @see Array.get
     * @param index The index to get the value at
     * @return The value at the given index
     */
    fun get(index: Int): T = array.get(index)

    /**
     * Gets the index of the given value.
     * @see Array.indexOf
     * @param value The value to get the index of
     * @param identity If the index should be found using identity
     */
    fun indexOf(value: T, identity: Boolean) = array.indexOf(value, identity)

    /**
     * Gets the last index of the given value.
     * @see Array.lastIndexOf
     * @param value The value to get the last index of
     * @param identity If the last index should be found using identity
     */
    fun lastIndexOf(value: T, identity: Boolean) = array.lastIndexOf(value, identity)

    /**
     * Returns if this array contains the given value.
     * @see Array.contains
     * @param value The value to check for
     * @param identity If the check should be done using identity
     */
    fun contains(value: T, identity: Boolean) = array.contains(value, identity)

    override fun clear() = array.clear()

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { array.add(it) }
        return true
    }

    override fun add(element: T): Boolean {
        array.add(element)
        return true
    }

    override fun isEmpty() = array.isEmpty

    override fun iterator() = array.iterator()

    override fun retainAll(elements: Collection<T>): Boolean {
        val iterator = array.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!elements.contains(next)) iterator.remove()
        }
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val iterator = array.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (elements.contains(next)) iterator.remove()
        }
        return true
    }

    override fun remove(element: T) = array.removeValue(element, false)

    override fun containsAll(elements: Collection<T>): Boolean {
        elements.forEach { if (!array.contains(it, false)) return false }
        return true
    }

    override fun contains(element: T) = array.contains(element, false)
}