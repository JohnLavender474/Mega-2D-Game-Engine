package com.engine.graph

import com.engine.common.extensions.toImmutableCollection
import com.engine.common.objects.ImmutableCollection
import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.GameShape2D

/**
 * A [GraphMap] that uses a quad tree to store and retrieve objects.
 *
 * @param width The width of the graph.
 * @param height The height of the graph.
 * @param ppm The number of pixels per meter.
 * @param depth The depth of the quad tree.
 */
open class QuadTreeGraphMap(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    override val ppm: Int,
    val depth: Int
) : GraphMap {

  protected val objects = HashMap<IntPair, ArrayList<Any>>()

  override fun get(x: Int, y: Int) =
      objects.getOrDefault(IntPair(x, y), emptySet()).toImmutableCollection()

  override fun get(minX: Int, minY: Int, maxX: Int, maxY: Int): ImmutableCollection<Any> {
    val set = HashSet<Any>()

    for (x in minX..maxX) {
      for (y in minY..maxY) {
        if (isOutOfBounds(x, y)) {
          continue
        }

        set.addAll(get(x, y))
      }
    }

    return set.toImmutableCollection()
  }

  /**
   * Adds the given object to this graph. This method is recursive. It will add the object to the
   * correct cell. If the recursive call is not at the maximum depth, it will split the cell into
   * four sub-cells and add the object to the correct sub-cell.
   *
   * @param obj The object to add.
   * @param currentDepth The currentDepth depth of the recursive call.
   * @param minX The minimum first coordinate of the cell.
   * @param minY The minimum second coordinate of the cell.
   * @param maxX The maximum first coordinate of the cell.
   * @param maxY The maximum second coordinate of the cell.
   */
  protected fun add(
      obj: Any,
      shape: GameShape2D,
      currentDepth: Int,
      minX: Int,
      minY: Int,
      maxX: Int,
      maxY: Int
  ): Boolean {
    val overlap =
        GameRectangle(minX * ppm, minY * ppm, (maxX - minX) * ppm, (maxY - minY) * ppm)
            .overlaps(shape)

    if (overlap) {
      if (currentDepth < depth) {
        val midX = (minX + maxX) / 2
        val midY = (minY + maxY) / 2

        add(obj, shape, currentDepth + 1, minX, minY, midX, midY)
        add(obj, shape, currentDepth + 1, midX, minY, maxX, midY)
        add(obj, shape, currentDepth + 1, minX, midY, midX, maxY)
        add(obj, shape, currentDepth + 1, midX, midY, maxX, maxY)
      } else {
        for (x in minX until maxX) {
          for (y in minY until maxY) {
            if (isOutOfBounds(x, y)) {
              continue
            }

            return objects.computeIfAbsent(IntPair(x, y)) { ArrayList() }.add(obj)
          }
        }
      }
    }

    return false
  }

  override fun add(obj: Any, shape: GameShape2D) = add(obj, shape, 0, 0, 0, width, height)

  override fun reset() = objects.clear()
}