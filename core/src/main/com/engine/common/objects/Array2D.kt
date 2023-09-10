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
class Array2D<T>(val rows: Int, val columns: Int) {

  val size: Int
    get() = array2DMap.size

  private val array2DMap = LinkedHashMap<IntPair, T>()
  private val elementToIndexMap = HashMap<T, HashSet<IntPair>>()

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

  fun contains(element: T) = elementToIndexMap.contains(element)

  fun containsAll(elements: Collection<T>) = elements.all { contains(it) }

  fun iterator() = Array2DIterator(this)

  fun isEmpty() = array2DMap.isEmpty()

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

  fun addAll(elements: Collection<T>) = elements.all { add(it) }

  fun add(element: T): Boolean {
    for (i in 0..rows) {
      for (j in 0..columns) {
        val indexPair = i pairTo j
        if (array2DMap[indexPair] == null) {
          array2DMap[indexPair] = element
          elementToIndexMap.getOrPut(element) { HashSet() }.add(indexPair)
          return true
        }
      }
    }
    return false
  }

  fun clear() {
    array2DMap.clear()
    elementToIndexMap.clear()
  }

  fun <R> map(transform: (T) -> R): Array2D<R> {
    val mappedArray2D = Array2D<R>(rows, columns)
    array2DMap.forEach { (indexPair, element) ->
      mappedArray2D[indexPair.x, indexPair.y] = transform(element)
    }
    return mappedArray2D
  }

  fun forEach(action: (IntPair, T?) -> Unit) = array2DMap.forEach(action)

  fun forEach(flipHorizontally: Boolean, flipVertically: Boolean, action: (IntPair, T?) -> Unit) {
    for (i in 0 until rows) {
      for (j in 0 until columns) {
        val row = if (flipVertically) rows - 1 - i else i
        val column = if (flipHorizontally) columns - 1 - j else j

        action(row pairTo column, this[i, j])
      }
    }
  }

  operator fun set(rowIndex: Int, columnIndex: Int, element: T) {
    if (rowIndex < 0 || rowIndex >= rows) {
      throw IndexOutOfBoundsException("Row index $rowIndex is out of bounds")
    }
    if (columnIndex < 0 || columnIndex >= columns) {
      throw IndexOutOfBoundsException("Column index $columnIndex is out of bounds")
    }
    val indexPair = rowIndex pairTo columnIndex
    array2DMap[indexPair] = element
    elementToIndexMap[element] =
        elementToIndexMap.getOrPut(element) { HashSet() }.apply { add(indexPair) }
  }

  operator fun get(rowIndex: Int, columnIndex: Int): T? = array2DMap[rowIndex pairTo columnIndex]

  fun getIndexes(element: T) = elementToIndexMap[element] ?: emptySet()

  fun removeAll(elements: Collection<T>): Boolean {
    elements.forEach { remove(it) }
    return true
  }

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
