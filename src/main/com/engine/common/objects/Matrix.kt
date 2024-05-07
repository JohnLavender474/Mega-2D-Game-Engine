package com.engine.common.objects

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedMap

/**
 * This iterator iterates through the elements of the Matrix beginning at index [0, 0] and moving to
 * the right, moving up each time a row is completely iterated through.
 *
 * @param T the type of element in the Matrix
 * @param matrix the Matrix to iterate through
 */
class MatrixIterator<T>(private val matrix: Matrix<T>) : MutableIterator<T> {
    var rowIndex = 0
        private set
    var columnIndex = -1
        private set

    override fun hasNext(): Boolean {
        if (rowIndex >= matrix.rows) return false

        if (columnIndex + 1 >= matrix.columns) {
            rowIndex++
            columnIndex = 0
            return hasNext()
        }

        return try {
            matrix[columnIndex + 1, rowIndex] != null
        } catch (_: Exception) {
            false
        }
    }

    override fun next(): T =
        try {
            columnIndex++
            val element = matrix[columnIndex, rowIndex]
            element!!
        } catch (e: Exception) {
            throw Exception("Could not get next element at row $rowIndex and column $columnIndex", e)
        }

    override fun remove() {
        matrix[columnIndex, rowIndex] = null
    }
}

/**
 * A matrix to store arrays. Null values are not counted as elements, so if all elements are null,
 * then the matrix is empty.
 *
 * @param rows the number of rows
 * @param columns the number of columns
 */
open class Matrix<T>(val rows: Int, val columns: Int) : MutableCollection<T> {

    override val size: Int
        get() = matrixMap.size

    internal val matrixMap = OrderedMap<IntPair, T>()
    internal val elementToIndexMap = ObjectMap<T, ObjectSet<IntPair>>()

    /**
     * Creates a [Matrix] using the provided 2D array. The array is copied in last to first order,
     * i.e. bottom to top. In other words, the last row inserted will be inserted as the first row in
     * the matrix.
     *
     * @param array the 2D array to copy
     */
    constructor(array: Array<Array<T>>) : this(array.size, array[0].size) {
        for (x in 0 until columns) {
            for (y in 0 until rows) {
                val row = rows - 1 - y
                set(x, y, array[row][x])
            }
        }
    }

    /**
     * Returns the element at the specified row and column. This method throws an [IndexOutOfBoundsException] if the
     * specified row or column is out of bounds. This method returns null if there is no element
     *
     * @return the element at the specified row and column, or null if there is no element
     * @throws IndexOutOfBoundsException if the specified row or column is out of bounds
     */
    operator fun get(column: Int, row: Int): T? {
        // Indexes must be within bounds
        if (isColumnOutOfBounds(column)) throw IndexOutOfBoundsException("Column index $column is out of bounds")
        if (isRowOutOfBounds(row)) throw IndexOutOfBoundsException("Row index $row is out of bounds")

        return matrixMap[column pairTo row]
    }

    /**
     * Sets the element at the specified x and row to the specified element. The x is equivalent to
     * the column, and the row is equivalent to the row. This method returns the old value at the
     * specified x and row. If the specified element is null, then the element at the x and row is
     * removed.
     */
    operator fun set(column: Int, row: Int, element: T?): T? {
        // Indexes must be within bounds
        if (isColumnOutOfBounds(column))
            throw IndexOutOfBoundsException("Column index $column is out of bounds")
        if (isRowOutOfBounds(row)) throw IndexOutOfBoundsException("Row index $row is out of bounds")

        // Convert row and column index to index pair
        val indexPair = column pairTo row

        // Remove the old value from the elementToIndexMap if it exists
        // This is done so that the elementToIndexMap does not contain any stale values
        val oldValue = matrixMap[indexPair]
        if (oldValue != null) {
            val oldValueSet = elementToIndexMap[oldValue]
            oldValueSet?.remove(indexPair)

            if (oldValueSet?.isEmpty == true) elementToIndexMap.remove(oldValue)
        } else matrixMap.remove(indexPair)

        // If the new value is null, then simply remove the index pair from the array 2D map,
        // otherwise add the index pair and element to the array 2D map and element to the element to
        // index map respectively
        if (element != null) {
            matrixMap.put(indexPair, element)
            if (!elementToIndexMap.containsKey(element)) elementToIndexMap.put(element, ObjectSet())
            val set = elementToIndexMap.get(element)
            set.add(indexPair)
        } else matrixMap.remove(indexPair)

        // Return the old value
        return oldValue
    }

    /**
     * Returns true if the specified row is out of bounds. This method returns true if the specified
     * row is less than 0 or greater than or equal to the number of rows. This method returns false
     * otherwise.
     *
     * @param rowIndex the row index
     * @return true if the specified row is out of bounds, false otherwise
     */
    fun isRowOutOfBounds(rowIndex: Int) = rowIndex < 0 || rowIndex >= rows

    /**
     * Returns true if the specified column is out of bounds. This method returns true if the
     * specified column is less than 0 or greater than or equal to the number of columns. This method
     * returns false otherwise.
     *
     * @param columnIndex the column index
     * @return true if the specified column is out of bounds, false otherwise
     */
    fun isColumnOutOfBounds(columnIndex: Int) = columnIndex < 0 || columnIndex >= columns

    /**
     * Returns true if the specified row or column are out of bounds. This method returns true if the
     * specified row is less than 0 or greater than or equal to the number of rows. This method
     * returns true if the specified column is less than 0 or greater than or equal to the number of
     * columns. This method returns false otherwise.
     *
     * @param columnIndex the column index
     * @param rowIndex the row index
     * @return true if the specified row or column are out of bounds, false otherwise
     */
    fun isOutOfBounds(columnIndex: Int, rowIndex: Int) =
        isRowOutOfBounds(rowIndex) || isColumnOutOfBounds(columnIndex)

    /**
     * Returns the indexes of the specified element. This method returns an empty set if the element
     * is not in the Array2D.
     *
     * @param element the element
     * @return the indexes of the specified element
     */
    fun getIndexes(element: T?) =
        if (element == null) {
            val nullIndexes = HashSet<IntPair>()
            for (x in 0 until columns) for (y in 0 until rows) if (this[x, y] == null)
                nullIndexes.add(x pairTo y)
            nullIndexes
        } else elementToIndexMap[element] ?: emptySet()

    /**
     * Applies the action to each element of the matrix including null elements. The first integer in
     * the action lambda is the x (column) and the second is the y (row) from bottom to top.
     *
     * @param action the action to apply to each element
     */
    fun forEach(action: ((Int, Int, T?) -> Unit)) {
        for (x in 0 until columns) for (y in 0 until rows) action(x, y, this[x, y])
    }

    override fun contains(element: T) = elementToIndexMap.containsKey(element)

    override fun containsAll(elements: Collection<T>) = elements.all { contains(it) }

    override fun clear() {
        matrixMap.clear()
        elementToIndexMap.clear()
    }

    override fun addAll(elements: Collection<T>) = elements.all { add(it) }

    override fun add(element: T): Boolean {
        for (x in 0 until columns) for (y in 0 until rows) if (matrixMap[x pairTo y] == null) {
            set(x, y, element)
            return true
        }

        return false
    }

    override fun isEmpty() = size == 0

    override fun iterator() = MatrixIterator(this)

    override fun retainAll(elements: Collection<T>): Boolean {
        var removed = false
        val toRemove = HashSet<T>()

        matrixMap.forEach { entry ->
            val e = entry.value
            e?.let {
                if (!elements.contains(it)) {
                    toRemove.add(it)
                    removed = true
                }
            }
        }

        toRemove.forEach { remove(it) }

        return removed
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        elements.forEach { remove(it) }
        return true
    }

    override fun remove(element: T): Boolean {
        val indexPairs = elementToIndexMap.remove(element) ?: return false
        indexPairs.forEach { matrixMap.remove(it) }

        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + matrixMap.hashCode()
        result = 31 * result + elementToIndexMap.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix<*>) {
            return false
        }
        for (x in 0 until columns) for (y in 0 until rows) if (this[x, y] != other[x, y]) return false
        return true
    }

    override fun toString(): String {
        val sb = StringBuilder()

        sb.append("[")
        for (y in rows - 1 downTo 0) {
            sb.append("[")
            for (x in 0 until columns) {
                sb.append(this[x, y])

                if (x < columns - 1) sb.append(", ")
            }
            sb.append("]")
            if (y > 0) sb.append(", ")
        }
        sb.append("]")

        return sb.toString()
    }
}
