package com.engine.world

import com.badlogic.gdx.math.Rectangle
import com.engine.common.interfaces.Resettable
import com.engine.common.shapes.GameLine
import com.engine.common.shapes.GameRectangle
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * A graph of the world that can be used to find bodies and fixtures in the world.
 *
 * @param width the tile width of the world
 * @param height the tile height of the world
 * @param ppm the pixels per meter (tile) of the world
 */
class WorldGraph(val width: Int, val height: Int, val ppm: Int) : Resettable {

  private val bodies: HashMap<Pair<Int, Int>, ArrayList<Body>> = HashMap()
  private val fixtures: HashMap<Pair<Int, Int>, ArrayList<Fixture>> = HashMap()

  /**
   * Adds the given body to this world graph.
   *
   * @param body the body to add
   * @return this world graph
   */
  fun addBody(body: Body): WorldGraph {
    val m = getMinsAndMaxes(body)
    for (x in m[0] until m[2]) {
      for (y in m[1] until m[3]) {
        bodies.computeIfAbsent(Pair(x, y)) { ArrayList() }.add(body)
      }
    }
    return this
  }

  /**
   * Adds the given bodies to this world graph.
   *
   * @param bodies the bodies to add
   * @return this world graph
   */
  fun addBodies(bodies: Collection<Body>) = bodies.forEach { addBody(it) }

  /**
   * Adds the given fixture to this world graph.
   *
   * @param fixture the fixture to add
   * @return this world graph
   */
  fun addFixture(fixture: Fixture): WorldGraph {
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
    return this
  }

  /**
   * Adds the given fixtures to this world graph.
   *
   * @param fixtures the fixtures to add
   * @return this world graph
   */
  fun addFixtures(fixtures: Collection<Fixture>): WorldGraph {
    fixtures.forEach { addFixture(it) }
    return this
  }

  /**
   * Returns the [ArrayList] of [Fixture]s at the given position.
   *
   * @param x the x position of the tile to fetch from
   * @param y the y position of the tile to fetch from
   */
  fun getFixtures(x: Int, y: Int) = fixtures.getOrDefault(Pair(x, y), ArrayList())

  /**
   * Returns the [ArrayList] of [Body]s at the given position.
   *
   * @param x the x position of the tile to fetch from
   * @param y the y position of the tile to fetch from
   */
  fun getBodies(x: Int, y: Int) = bodies.getOrDefault(Pair(x, y), ArrayList())

  /**
   * Returns the [ArrayList] of [Body]s overlapping the given body.
   *
   * @param body the body to check for overlapping bodies
   * @param filter the filter to apply to the overlapping bodies
   * @return the [ArrayList] of [Body]s overlapping the given body
   * @see Body
   * @see Fixture
   */
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
          if (body != it && !checked.contains(it)) {
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

  /**
   * Returns the [ArrayList] of [Fixture]s overlapping the given fixture.
   *
   * @param fixture the fixture to check for overlapping fixtures
   * @param filter the filter to apply to the overlapping fixtures
   * @return the [ArrayList] of [Fixture]s overlapping the given fixture
   * @see Fixture
   */
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
          if (fixture != it && !checked.contains(it)) {
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

  /** Clears the bodies and fixtures from this world graph. Called from within the [WorldSystem]. */
  override fun reset() {
    bodies.clear()
    fixtures.clear()
  }
}
