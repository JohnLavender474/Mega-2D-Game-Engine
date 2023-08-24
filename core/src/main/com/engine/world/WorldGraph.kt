package com.engine.world

import com.badlogic.gdx.math.Rectangle
import com.engine.common.interfaces.Resettable
import com.engine.common.shapes.GameLine
import com.engine.common.shapes.GameRectangle
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class WorldGraph(val width: Int, val height: Int, val ppm: Int) : Resettable {

  private val bodies: HashMap<Pair<Int, Int>, ArrayList<Body>> = HashMap()
  private val fixtures: HashMap<Pair<Int, Int>, ArrayList<Fixture>> = HashMap()

  fun addBody(body: Body) {
    val m = getMinsAndMaxes(body)
    for (x in m[0] until m[2]) {
      for (y in m[1] until m[3]) {
        bodies.computeIfAbsent(Pair(x, y)) { ArrayList() }.add(body)
      }
    }
  }

  fun addFixture(fixture: Fixture) {
    val bounds =
        fixture.shape.let {
          when (it) {
            is GameRectangle -> it
            is GameLine -> it.getBoundingRectangle()
            else -> null
          }
        }
    bounds?.let {
      val m = getMinsAndMaxes(bounds)
      for (x in m[0] until m[2]) {
        for (y in m[1] until m[3]) {
          fixtures.computeIfAbsent(Pair(x, y)) { ArrayList() }.add(fixture)
        }
      }
    }
  }

  fun getFixtures(x: Int, y: Int) = fixtures.getOrDefault(Pair(x, y), ArrayList())

  fun getBodies(x: Int, y: Int) = bodies.getOrDefault(Pair(x, y), ArrayList())

  fun getBodiesOverlapping(body: Body, filter: ((Body) -> Boolean)? = null): ArrayList<Body> {
    val m = getMinsAndMaxes(body)
    val xMin = m[0]
    val yMin = m[1]
    val xMax = m[2]
    val yMax = m[3]
    val overlapping = ArrayList<Body>()
    val checked = HashSet<Body>()
    for (x in xMin until xMax) {
      for (y in yMin until yMax) {
        val c = Pair(x, y)
        bodies[c]?.forEach {
          if (!checked.contains(it)) {
            checked.add(it)
            if (body.overlaps(it as Rectangle) && filter?.invoke(it) != false) {
              overlapping.add(it)
            }
          }
        }
      }
    }
    return overlapping
  }

  fun getFixturesOverlapping(
      fixture: Fixture,
      filter: ((Fixture) -> Boolean)? = null
  ): ArrayList<Fixture> {
    val m = getMinsAndMaxes(fixture.shape.getBoundingRectangle())
    val xMin = m[0]
    val yMin = m[1]
    val xMax = m[2]
    val yMax = m[3]
    val overlapping = ArrayList<Fixture>()
    val checked = HashSet<Fixture>()
    for (x in xMin until xMax) {
      for (y in yMin until yMax) {
        val c = Pair(x, y)
        fixtures[c]?.forEach {
          if (!checked.contains(it)) {
            checked.add(it)
            if (fixture.overlaps(it) && filter?.invoke(it) != false) {
              overlapping.add(it)
            }
          }
        }
      }
    }
    return overlapping
  }

  private fun getMinsAndMaxes(bounds: Rectangle): IntArray {
    val minX = floor(bounds.x / ppm).toInt() - 1
    val minY = floor(bounds.y / ppm).toInt() - 1
    val maxX = ceil((bounds.x + bounds.width) / ppm).toInt() + 1
    val maxY = ceil((bounds.y + bounds.height) / ppm).toInt() + 1
    return intArrayOf(max(0, minX), max(0, minY), min(width, maxX), min(height, maxY))
  }

  override fun reset() {
    bodies.clear()
    fixtures.clear()
  }
}
