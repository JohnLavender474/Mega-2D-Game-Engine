package com.engine.common.objects

/**
 * An iterator for [Array2D]. This iterator iterates through the elements of the Array2D in row
 * major order. This means that the iterator will iterate through the elements of the first row,
 * then the second row, and so on. The iterator will iterate through the elements of the first
 * column, then the second column, and so on.
 *
 * @param T the type of elements in the Array2D
 * @param array2D the Array2D to iterate through
 */
@Deprecated("Use Matrix instead.", ReplaceWith("Matrix Iterator"))
class Array2DIterator<T : Any?>(private val array2D: Array2D<T>) {

    var rowIndex = 0
        private set
    var columnIndex = 0
        private set

    /**
     * Returns true if the iterator has more elements. This method will return false if the iterator
     * has already iterated through all the elements of the Array2D.
     *
     * @return true if the iterator has more elements, false otherwise
     */
    fun hasNext(): Boolean {
        if (rowIndex >= array2D.rows) return false
        if (columnIndex >= array2D.columns) {
            rowIndex++
            columnIndex = 0
            return hasNext()
        }
        return true
    }

    /**
     * Returns the next element in the iteration. This method will throw a [NoSuchElementException] if
     * there are no more elements to iterate through.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if there are no more elements to iterate through
     */
    fun next(): T? {
        if (!hasNext()) {
            throw NoSuchElementException("There are no more elements")
        }
        val element = array2D[rowIndex, columnIndex]
        columnIndex++
        return element
    }
}

/**
 * A 2D array that can be used to store elements of any type. Although the type parameter is
 * restricted to non-nullable types, the array itself can only store nullable elements. This is
 * because the array is initialized with null values and the type parameter is used to restrict the
 * type of elements that can be added to the array.
 *
 * @param T the type of elements in the array
 * @constructor Creates an Array2D with the specified number of rows and columns
 */
@Deprecated(
    "Should use Matrix instead. By default, Matrix iterates from bottom to top. " +
            "This is almost always the desired behavior.", ReplaceWith("Matrix")
)
class Array2D<T>(val rows: Int, val columns: Int) {

    val size: Int
        get() = rows * columns

    internal val array2DMap = LinkedHashMap<IntPair, T>()
    internal val elementToIndexMap = HashMap<T, HashSet<IntPair>>()

    /**
     * Creates an Array2D using the provided 2D array. The array is copied into the Array2D. If the
     * array is not rectangular, an exception will be thrown. If [flipVertically] is true, the array
     * will be flipped vertically. This means that the first row of the array will be the last row of
     * the Array2D and the last row of the array will be the first row of the Array2D.
     *
     * @param array the 2D array to copy into the Array2D
     * @param flipVertically whether to flip the array vertically
     */
    constructor(
        array: Array<Array<T>>,
        flipVertically: Boolean = false,
        flipHorizontally: Boolean = false,
    ) : this(array.size, array[0].size) {
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val row = if (flipVertically) rows - 1 - i else i
                val column = if (flipHorizontally) columns - 1 - j else j

                this[row, column] = array[i][j]
            }
        }
    }

    /**
     * Returns true if the Array2D contains the specified element.
     *
     * @param element the element to check for
     * @return true if the Array2D contains the specified element, false otherwise
     */
    fun contains(element: T) = elementToIndexMap.contains(element)

    /**
     * Returns true if the Array2D contains all the specified elements.
     *
     * @param elements the elements to check for
     * @return true if the Array2D contains all the specified elements, false otherwise
     */
    fun containsAll(elements: Collection<T>) = elements.all { contains(it) }

    /**
     * Returns an iterator over the elements of the Array2D. The elements are iterated through in row
     * major order. This means that the iterator will iterate through the elements of the first row,
     * then the second row, and so on. The iterator will iterate through the elements of the first
     * column, then the second column, and so on.
     *
     * @return an iterator over the elements of the Array2D
     * @see Array2DIterator
     */
    fun iterator() = Array2DIterator(this)

    /**
     * Removes elements from this Array2D that are not contained in the specified collection. This
     * method returns true if any elements were removed from this Array2D.
     *
     * @param elements the collection of elements to retain
     * @return true if any elements were removed from this Array2D, false otherwise
     * @see [remove]
     */
    fun retainAll(elements: Collection<T>): Boolean {
        var removed = false
        val toRemove = HashSet<T>()

        array2DMap.forEach { (_, e) ->
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

    /**
     * Adds all the specified elements to this Array2D. This method returns true if all elements were
     * added to this Array2D. An element is not added if this Array2D is already full, i.e. there are
     * no more null elements.
     *
     * @param elements the elements to add
     * @return true if all elements were added to this Array2D, false otherwise
     * @see [add]
     */
    fun addAll(elements: Collection<T>) = elements.all { add(it) }

    /**
     * Adds the specified element to this Array2D. This method returns true if the element was added
     * to this Array2D. The element is not added if this Array2D is already full, i.e. there are no
     * more null elements. Inside this function, the Array2D is iterated through in row major order.
     * This means that the first row is iterated through, then the second row, and so on. The first
     * column is iterated through, then the second column, and so on. The first null element is set
     * with the specified element.
     *
     * @param element the element to add
     * @return true if the element was added to this Array2D, false otherwise
     * @see [set]
     */
    fun add(element: T): Boolean {
        for (i in 0..rows) {
            for (j in 0..columns) {
                if (array2DMap[i pairTo j] == null) {
                    set(i, j, element)

                    return true
                }
            }
        }

        return false
    }

    /** Removes all the elements from this Array2D. */
    fun clear() {
        array2DMap.clear()
        elementToIndexMap.clear()
    }

    /**
     * Maps the elements of this Array2D to a new Array2D using the specified transform function. This
     * method returns a new Array2D with the same dimensions as this Array2D. The transform function
     * is applied to each element in this Array2D and the result is stored in the new Array2D. The
     * transform function is applied in row major order. This means that the first row is iterated
     * through, then the second row, and so on. The first column is iterated through, then the second
     * column, and so on.
     *
     * @param transform the transform function
     * @return a new Array2D with the same dimensions as this Array2D
     */
    fun <R> map(transform: (T) -> R): Array2D<R> {
        val mappedArray2D = Array2D<R>(rows, columns)

        array2DMap.forEach { (indexPair, element) ->
            mappedArray2D[indexPair.first, indexPair.second] = transform(element)
        }

        return mappedArray2D
    }

    /**
     * Applies the specified action to each element in this Array2D. The action is applied in row
     * major order. This means that the first row is iterated through, then the second row, and so on.
     * Keep in mind that for the [action], the first integer value is the row (y value) and the second
     * is the column (x value).
     *
     * @param action the action to apply
     */
    fun forEach(action: (Int, Int, T?) -> Unit) =
        array2DMap.forEach { (indexPair, element) ->
            action(indexPair.first, indexPair.second, element)
        }

    /**
     * Applies the specified action to each element in this Array2D. This method has two parameters
     * that allow for flipping the iteration. If [flipHorizontally] is true, the iteration will be
     * flipped horizontally. This means that the first column will be iterated through, then the
     * second column, and so on. If [flipVertically] is true, the iteration will be flipped
     * vertically. This means the first row will be iterated through, then the second, and so on. Keep
     * in mind that for the [action], the first integer value is the row (y value) and the second is
     * the column (x value).
     *
     * @param flipHorizontally whether to flip the iteration horizontally
     * @param flipVertically whether to flip the iteration vertically
     * @param action the action to apply
     * @see [forEach]
     */
    fun forEach(flipHorizontally: Boolean, flipVertically: Boolean, action: (Int, Int, T?) -> Unit) {
        array2DMap.forEach { (indexPair, element) ->
            val row = if (flipVertically) rows - 1 - indexPair.first else indexPair.first
            val column = if (flipHorizontally) columns - 1 - indexPair.second else indexPair.second

            action(row, column, element)
        }
    }

    /**
     * Returns a new Array2D where the elements are flipped horizontally.
     *
     * @return a new Array2D where the elements are flipped horizontally
     */
    fun flipHorizontally(): Array2D<T> {
        val flippedArray2D = Array2D<T>(rows, columns)

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                flippedArray2D[i, j] = this[i, columns - 1 - j]
            }
        }

        return flippedArray2D
    }

    /**
     * Returns a new Array2D where the elements are flipped vertically.
     *
     * @return a new Array2D where the elements are flipped vertically
     */
    fun flipVertically(): Array2D<T> {
        val flippedArray2D = Array2D<T>(rows, columns)

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                flippedArray2D[i, j] = this[rows - 1 - i, j]
            }
        }

        return flippedArray2D
    }

    /**
     * Returns a new Array2D where the elements are flipped horizontally and vertically.
     *
     * @return a new Array2D where the elements are flipped horizontally and vertically
     */
    fun flip(): Array2D<T> {
        val v = flipHorizontally()
        return v.flipVertically()
    }

    /**
     * Sets the element at the specified row and column to the specified element. This method throws
     * an [IndexOutOfBoundsException] if the specified row or column is out of bounds. This method
     * also throws an [IllegalArgumentException] if the specified element is null. This method returns
     * the element that was previously at the specified row and column. If there was no element at the
     * specified row and column, null is returned.
     *
     * @param rowIndex the row index of the element to set
     * @param columnIndex the column index of the element to set
     * @param element the element to set
     * @return the element that was previously at the specified row and column, or null if there was
     */
    operator fun set(rowIndex: Int, columnIndex: Int, element: T?): T? {
        // Indexes must be within bounds
        if (isRowOutOfBounds(rowIndex)) {
            throw IndexOutOfBoundsException("Row index $rowIndex is out of shape")
        }
        if (isColumnOutOfBounds(columnIndex)) {
            throw IndexOutOfBoundsException("Column index $columnIndex is out of shape")
        }

        // Convert row and column index to index pair
        val indexPair = rowIndex pairTo columnIndex

        // Remove the old value from the elementToIndexMap if it exists
        // This is done so that the elementToIndexMap does not contain any stale values
        val oldValue = array2DMap[indexPair]
        if (oldValue != null) {
            val oldValueSet = elementToIndexMap[oldValue]
            oldValueSet?.remove(indexPair)
            if (oldValueSet?.isEmpty() == true) elementToIndexMap.remove(oldValue)
        } else {
            array2DMap.remove(indexPair)
        }

        // If the new value is null, then simply remove the index pair from the array 2D map,
        // otherwise add the index pair and element to the array 2D map and element to the element to
        // index map respectively
        if (element != null) {
            array2DMap[indexPair] = element
            elementToIndexMap[element] =
                elementToIndexMap.getOrPut(element) { HashSet() }.apply { add(indexPair) }
        } else {
            array2DMap.remove(indexPair)
        }

        // Return the old value
        return oldValue
    }

    /**
     * Returns the element at the specified row and column. This method throws an
     * [IndexOutOfBoundsException] if the specified row or column is out of bounds. This method
     * returns null if there is no element
     */
    operator fun get(rowIndex: Int, columnIndex: Int): T? {
        // Indexes must be within bounds
        if (isRowOutOfBounds(rowIndex)) {
            throw IndexOutOfBoundsException("Row index $rowIndex is out of shape")
        }
        if (isColumnOutOfBounds(columnIndex)) {
            throw IndexOutOfBoundsException("Column index $columnIndex is out of shape")
        }

        return array2DMap[rowIndex pairTo columnIndex]
    }

    /**
     * Returns true if the specified row or column are out of bounds. This method returns true if the
     * specified row is less than 0 or greater than or equal to the number of rows. This method
     * returns true if the specified column is less than 0 or greater than or equal to the number of
     * columns. This method returns false otherwise.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return true if the specified row or column are out of bounds, false otherwise
     */
    fun isOutOfBounds(rowIndex: Int, columnIndex: Int) =
        isRowOutOfBounds(rowIndex) || isColumnOutOfBounds(columnIndex)

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
     * Returns the indexes of the specified element. This method returns an empty set if the element
     * is not in the Array2D.
     *
     * @param element the element
     * @return the indexes of the specified element
     */
    fun getIndexes(element: T?) =
        if (element == null) {
            val nullIndexes = HashSet<IntPair>()

            for (i in 0 until rows) {
                for (j in 0 until columns) {
                    if (this[i, j] == null) {
                        nullIndexes.add(i pairTo j)
                    }
                }
            }

            nullIndexes
        } else {
            elementToIndexMap[element] ?: emptySet()
        }

    /**
     * Removes all the specified elements from this Array2D.
     *
     * @param elements the elements to remove
     * @return true if any elements were removed from this Array2D, false otherwise
     */
    fun removeAll(elements: Collection<T>): Boolean {
        elements.forEach { remove(it) }
        return true
    }

    /**
     * Removes the specified element from this Array2D. This method returns true if the element was
     * removed from this Array2D. This method returns false if the element was not in this Array2D.
     *
     * @param element the element to remove
     * @return true if the element was removed from this Array2D, false otherwise
     */
    fun remove(element: T): Boolean {
        val indexPairs = elementToIndexMap.remove(element) ?: return false
        indexPairs.forEach { array2DMap.remove(it) }

        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + array2DMap.hashCode()
        result = 31 * result + elementToIndexMap.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Array2D<*>) {
            return false
        }

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                if (this[i, j] != other[i, j]) {
                    return false
                }
            }
        }

        return true
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                sb.append(this[i, j])

                if (j < columns - 1) {
                    sb.append(", ")
                }
            }

            if (i < rows - 1) {
                sb.append("\n")
            }
        }

        return sb.toString()
    }
}
