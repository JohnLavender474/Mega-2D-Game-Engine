package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.engine.common.objects.Array2D
import com.engine.common.objects.IntPair
import com.engine.common.objects.pairTo
import com.engine.graph.GraphMap
import com.engine.graph.SimpleMockGraphMap
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks

/**
 * Tests for the [Pathfinder] class. These tests are not exhaustive, but they should be enough to
 * ensure that the [Pathfinder] class works as intended. The [Pathfinder] class is used to find a
 * graphPath from a start point to a target point. It uses the [GraphMap] class to represent the
 * world. The [GraphMap] class is used to store and retrieve objects in the world. The [Pathfinder]
 * class uses the [GraphMap] class to determine if a graphPath exists from the start point to the
 * target point. If a graphPath exists, the [Pathfinder] class returns a collection of [IntPair]s
 * that represent the graphPath from the start point to the target point
 *
 * @see [Pathfinder]
 * @see [GraphMap]
 */
class PathfinderTest :
    DescribeSpec({
      lateinit var graph: GraphMap

      /**
       * Creates a [PathfinderParams] instance from the given matrix and whether diagonal movement
       * is allowed. The matrix is used to determine the start point, target point, and obstacles.
       * The start point is represented by an S, the target point is represented by a T, and
       * obstacles are represented by !s. The 0s represent empty spaces.
       *
       * @param matrix the matrix that represents the world
       * @param allowDiagonal whether diagonal movement is allowed
       * @return a [PathfinderParams] instance
       */
      fun createPathfinderParams(
          matrix: Array2D<String>,
          allowDiagonal: Boolean
      ): PathfinderParams {
        lateinit var startSupplier: () -> Vector2
        lateinit var targetSupplier: () -> Vector2
        val nodesToFilter = HashSet<IntPair>()

        matrix.forEach(
            flipHorizontally = false,
            flipVertically = true,
            action = { coordinate: IntPair, value: String? ->
              val (row, column) = coordinate

              when (value) {
                "S" -> {
                  println("Start supplier: ${row pairTo column}")
                  startSupplier = {
                    Vector2(column.toFloat() * graph.ppm, row.toFloat() * graph.ppm)
                  }
                }
                "T" -> {
                  println("Target supplier: ${row pairTo column}")
                  targetSupplier = {
                    Vector2(column.toFloat() * graph.ppm, row.toFloat() * graph.ppm)
                  }
                }
                "!" -> nodesToFilter.add(row pairTo column)
                else -> {}
              }
            })

        println("Nodes to filter: $nodesToFilter")
        val filter: (IntPair, Collection<Any>) -> Boolean = { coordinate, _ ->
          !nodesToFilter.contains(coordinate)
        }

        return PathfinderParams(startSupplier, targetSupplier, { allowDiagonal }, filter)
      }

      describe("Pathfinder") {
        beforeEach { clearAllMocks() }

        it("should find the fastest path - test 1") {
          // If
          graph = SimpleMockGraphMap(0, 0, 4, 4, 16)
          val world =
              Array2D(
                  arrayOf(
                      arrayOf("S", "0", "0", "0"),
                      arrayOf("0", "0", "0", "0"),
                      arrayOf("0", "0", "0", "0"),
                      arrayOf("0", "0", "0", "T"),
                  ),
                  flipVertically = true)
          println("World: \n$world")

          val params = createPathfinderParams(world, true)
          println("Params: $params")

          val pathfinder = Pathfinder(graph, params)

          // when
          val result = pathfinder.call()
          println(result)

          // then
          result.graphPath shouldNotBe null
          result.worldPath shouldNotBe null

          result.graphPath?.size shouldBe 4
          result.worldPath?.size shouldBe 4

          for (i in 0 until 3) {
            result.graphPath?.get(i) shouldBe IntPair(i, i)
            result.worldPath?.get(i) shouldBe
                Vector2(i * graph.ppm.toFloat(), i * graph.ppm.toFloat())
          }
        }
      }
    })
