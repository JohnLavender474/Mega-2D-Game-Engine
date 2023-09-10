package com.engine.graph

import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameShape2D

/**
 * A simple implementation of [GraphMap] that stores objects in a map. This implementation is not
 * very efficient, but it is useful for testing. It is not recommended to use this implementation.
 *
 * @param width the width of the graph
 * @param height the height of the graph
 * @param ppm the number of pixels per meter
 */
class SimpleMockGraphMap(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    override val ppm: Int
) : GraphMap {

  private val objs = HashMap<IntPair, MutableList<Any>>()

  override fun add(obj: Any, shape: GameShape2D): Boolean {
    val bounds = shape.getBoundingRectangle()

    val x = (bounds.x / ppm).toInt()
    val y = (bounds.y / ppm).toInt()
    val maxX = (bounds.getMaxX() / ppm).toInt()
    val maxY = (bounds.getMaxY() / ppm).toInt()

    for (i in x..maxX) {
      for (j in y..maxY) {
        objs.getOrPut(IntPair(i, j)) { ArrayList() }.add(obj)
      }
    }

    return true
  }

  override fun get(x: Int, y: Int) = objs.getOrDefault(IntPair(x, y), emptyList())

  override fun get(minX: Int, minY: Int, maxX: Int, maxY: Int): Collection<Any> {
    val objs = HashSet<Any>()

    for (i in minX..maxX) {
      for (j in minY..maxY) {
        objs.addAll(get(i, j))
      }
    }

    return objs
  }

  override fun reset() = objs.clear()
}
