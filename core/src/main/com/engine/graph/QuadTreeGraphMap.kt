package com.engine.graph

import com.engine.common.extensions.toImmutableCollection
import com.engine.common.objects.ImmutableCollection
import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.GameShape2DSupplier

open class QuadTreeGraphMap(
    override val width: Int,
    override val height: Int,
    override val ppm: Int,
    val depth: Int
) : GraphMap {

  protected val objects = HashMap<IntPair, ArrayList<GameShape2DSupplier>>()

  protected fun add(
      obj: GameShape2DSupplier,
      current: Int,
      minX: Int,
      minY: Int,
      maxX: Int,
      maxY: Int
  ) {
    val overlap =
        GameRectangle(minX, minY, (maxX - minX) * ppm, (maxY - minY) * ppm)
            .overlaps(obj.getGameShape2D())

    if (overlap) {
      if (current < depth) {
        val midX = (minX + maxX) / 2
        val midY = (minY + maxY) / 2

        add(obj, current + 1, minX, minY, midX, midY)
        add(obj, current + 1, midX, minY, maxX, midY)
        add(obj, current + 1, minX, midY, midX, maxY)
        add(obj, current + 1, midX, midY, maxX, maxY)
      } else {
        for (x in minX until maxX) {
          for (y in minY until maxY) {
            objects.computeIfAbsent(IntPair(x, y)) { ArrayList() }.add(obj)
          }
        }
      }
    }
  }

  override fun add(obj: GameShape2DSupplier) = add(obj, 0, 0, 0, width, height)

  override fun get(x: Int, y: Int) =
      objects.getOrDefault(IntPair(x, y), emptySet()).toImmutableCollection()

  override fun get(minX: Int, minY: Int, maxX: Int, maxY: Int): ImmutableCollection<Any> {
    val set = HashSet<Any>()

    for (x in minX..maxX) {
      for (y in minY..maxY) {
        set.addAll(get(x, y))
      }
    }

    return set.toImmutableCollection()
  }

  override fun reset() = objects.clear()
}
