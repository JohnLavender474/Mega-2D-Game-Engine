package com.engine.common.objects

class Array2DIterator<T : Any?>(private val array2D: Array2D<T>) {

  var rowIndex = 0
    private set
  var columnIndex = 0
    private set

  fun hasNext(): Boolean {
    if (rowIndex >= array2D.rows) return false
    if (columnIndex >= array2D.columns) {
      rowIndex++
      columnIndex = 0
      return hasNext()
    }
    return true
  }

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

  private val array2DMap = LinkedHashMap<Pair<Int, Int>, T>()
  private val elementToIndexMap = HashMap<T, HashSet<Pair<Int, Int>>>()

  constructor(array: Array<Array<T>>): this(array.size, array[0].size) {
    for (i in 0 until rows) {
      for (j in 0 until columns) {
        this[i, j] = array[i][j]
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
        val indexPair = Pair(i, j)
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
      mappedArray2D[indexPair.first, indexPair.second] = transform(element)
    }
    return mappedArray2D
  }

  fun forEach(action: (Pair<Int, Int>, T?) -> Unit) = array2DMap.forEach(action)

  operator fun set(rowIndex: Int, columnIndex: Int, element: T) {
    if (rowIndex < 0 || rowIndex >= rows) {
      throw IndexOutOfBoundsException("Row index $rowIndex is out of bounds")
    }
    if (columnIndex < 0 || columnIndex >= columns) {
      throw IndexOutOfBoundsException("Column index $columnIndex is out of bounds")
    }
    val indexPair = Pair(rowIndex, columnIndex)
    array2DMap[indexPair] = element
    elementToIndexMap[element] =
        elementToIndexMap.getOrPut(element) { HashSet() }.apply { add(indexPair) }
  }

  operator fun get(rowIndex: Int, columnIndex: Int): T? = array2DMap[Pair(rowIndex, columnIndex)]

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
}
