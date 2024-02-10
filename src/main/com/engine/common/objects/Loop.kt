package com.engine.common.objects

import com.badlogic.gdx.utils.Array
import com.engine.common.interfaces.Resettable

/**
 * A loop that uses [next] to loop through its elements. When the end of the loop is reached, then
 * [next] will start from the beginning again.
 *
 * @param T the type of elements in this loop
 */
class Loop<T> : Resettable {

    private var array = Array<T>()
    private var index = 0

    val size: Int
        get() = array.size

    /**
     * Creates a loop with the specified elements. The elements will be added to the loop in the order
     * they are specified.
     *
     * @param startBeforeFirst whether to start the loop before the first element
     * @param elements the elements to add to the loop
     */
    constructor(
        elements: Array<T>,
        startBeforeFirst: Boolean = false,
    ) {
        array = Array(elements)
        index = if (startBeforeFirst) -1 else 0
    }

    /**
     * Creates a loop with the specified elements. The elements will be added to the loop in the order
     * they are returned by the specified loop's iterator. The index of the specified loop will be
     * copied to this loop.
     *
     * @param loop the loop whose elements to add to this loop
     */
    constructor(loop: Loop<T>) {
        array = Array(loop.array)
        index = loop.index
    }

    /**
     * Sets the index of this loop to the specified index.
     *
     * @param index the index to set
     */
    fun setIndex(index: Int) {
        this.index = index
    }

    /**
     * Sets the index of this loop to the next element in the loop and returns that element.
     *
     * @return the next element in the loop
     */
    fun next(): T {
        if (size == 0) throw NoSuchElementException("The loop is empty.")
        if (index >= size - 1) index = 0 else index++
        val value = array[index]
        return value
    }

    /**
     * Returns whether this loop is before the first element.
     *
     * @return whether this loop is before the first element
     */
    fun isBeforeFirst() = index == -1

    /**
     * Returns the current element in the loop.
     *
     * @return the current element in the loop
     */
    fun getCurrent(): T {
        if (size == 0) throw NoSuchElementException("The loop is empty.")
        if (isBeforeFirst())
            throw NoSuchElementException(
                "The loop is before the first element. Must call 'next()' first"
            )
        return array[index]
    }

    override fun reset() {
        index = 0
    }

    override fun toString() = "Loop(array=$array, index=$index)"

    override fun hashCode() = array.hashCode()

    override fun equals(other: Any?) = other is Loop<*> && array == other.array
}
