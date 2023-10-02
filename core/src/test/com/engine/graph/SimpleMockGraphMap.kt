package com.engine.graph

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
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

  private val objs = ObjectMap<IntPair, Array<Any>>()

  override fun add(obj: Any, shape: GameShape2D): Boolean {
    val bounds = shape.getBoundingRectangle()

    val x = (bounds.x / ppm).toInt()
    val y = (bounds.y / ppm).toInt()
    val maxX = (bounds.getMaxX() / ppm).toInt()
    val maxY = (bounds.getMaxY() / ppm).toInt()

    for (i in x..maxX) {
      for (j in y..maxY) {
        if (!objs.containsKey(IntPair(i, j))) objs.put(IntPair(i, j), Array())
        val array = objs.get(IntPair(i, j))
        array.add(obj)
      }
    }

    return true
  }

  override fun get(x: Int, y: Int): Array<Any> {
    if (objs.containsKey(IntPair(x, y))) {
      return objs.get(IntPair(x, y))
    }
    return Array()
  }

  override fun get(minX: Int, minY: Int, maxX: Int, maxY: Int): ObjectSet<Any> {
    val objs = ObjectSet<Any>()

    for (i in minX..maxX) {
      for (j in minY..maxY) {
        objs.addAll(get(i, j))
      }
    }

    return objs
  }

  override fun reset() = objs.clear()
}
