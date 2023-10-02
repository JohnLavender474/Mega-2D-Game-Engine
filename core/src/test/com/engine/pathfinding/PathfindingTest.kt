package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.engine.common.extensions.gdxArrayOf
import com.engine.common.objects.IntPair
import com.engine.common.objects.Matrix
import com.engine.common.objects.pairTo
import com.engine.graph.GraphMap
import com.engine.graph.SimpleMockGraphMap
import com.engine.graph.convertToWorldCoordinate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PathfinderTest :
    DescribeSpec({
      lateinit var graph: GraphMap

      fun createPathfinderParams(matrix: Matrix<String>, allowDiagonal: Boolean): PathfinderParams {
        lateinit var startSupplier: () -> Vector2
        lateinit var targetSupplier: () -> Vector2
        val nodesToFilter = HashSet<IntPair>()

        matrix.forEach(
            action = { x: Int, y: Int, value: String? ->
              when (value) {
                "S" -> {
                  startSupplier = { Vector2(x.toFloat() * graph.ppm, y.toFloat() * graph.ppm) }
                }
                "T" -> {
                  targetSupplier = { Vector2(x.toFloat() * graph.ppm, y.toFloat() * graph.ppm) }
                }
                "X" -> {
                  startSupplier = { Vector2(x.toFloat() * graph.ppm, y.toFloat() * graph.ppm) }
                  targetSupplier = { Vector2(x.toFloat() * graph.ppm, y.toFloat() * graph.ppm) }
                }
                "!" -> nodesToFilter.add(x pairTo y)
              }
            })

        val filter: (IntPair, Iterable<Any>) -> Boolean = { coordinate, _ ->
          !nodesToFilter.contains(coordinate)
        }

        return PathfinderParams(startSupplier, targetSupplier, { allowDiagonal }, filter)
      }

      it("createPathfinderParams") {

        // If
        graph = SimpleMockGraphMap(0, 0, 3, 2, 1)
        val array =
            gdxArrayOf(
                gdxArrayOf("S", "0", "0"),
                gdxArrayOf("0", "0", "T"),
            )
        val world = Matrix(array)

        // when
        val params = createPathfinderParams(world, true)

        // then
        world.forEach { x, y, element ->
          when (element) {
            "S" -> {
              x shouldBe 0
              y shouldBe 1
            }
            "T" -> {
              x shouldBe 2
              y shouldBe 0
            }
            else -> {}
          }
        }

        params.startSupplier() shouldBe Vector2(0f, 1f)
        params.targetSupplier() shouldBe Vector2(2f, 0f)
      }

      describe("Pathfinder") {
        it("should find the fastest path - test 1") {
          // If
          graph = SimpleMockGraphMap(0, 0, 4, 4, 16)
          val world =
              Matrix(
                  gdxArrayOf(
                      gdxArrayOf("S", "0", "0", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                      gdxArrayOf("0", "0", "0", "T"),
                  ))

          val params = createPathfinderParams(world, true)
          val pathfinder = Pathfinder(graph, params)

          // when
          val result = pathfinder.call()

          val graphPath = result.graphPath
          val worldPath = result.worldPath

          // then
          graphPath shouldNotBe null
          worldPath shouldNotBe null

          graphPath!!.size shouldBe 4
          worldPath!!.size shouldBe 4

          graphPath[3] shouldBe IntPair(0, 3)
          graphPath[2] shouldBe IntPair(1, 2)
          graphPath[1] shouldBe IntPair(2, 1)
          graphPath[0] shouldBe IntPair(3, 0)

          for (i in 0..3) {
            val graphPoint = graphPath[i]
            worldPath[i] shouldBe graph.convertToWorldCoordinate(graphPoint)
          }
        }

        it("should find the fastest path - test 2") {
          // If
          graph = SimpleMockGraphMap(0, 0, 4, 4, 16)
          val world =
              Matrix(
                  gdxArrayOf(
                      gdxArrayOf("S", "0", "!", "T"),
                      gdxArrayOf("!", "0", "!", "0"),
                      gdxArrayOf("0", "0", "!", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                  ))

          val params = createPathfinderParams(world, true)
          val pathfinder = Pathfinder(graph, params)

          // when
          val result = pathfinder.call()

          val graphPath = result.graphPath

          // then
          graphPath shouldNotBe null
          graphPath!!.size shouldBe 7

          graphPath[6] shouldBe IntPair(0, 3)
          graphPath[5] shouldBe IntPair(1, 2)
          graphPath[4] shouldBe IntPair(1, 1)
          graphPath[3] shouldBe IntPair(2, 0)
          graphPath[2] shouldBe IntPair(3, 1)
          graphPath[1] shouldBe IntPair(3, 2)
          graphPath[0] shouldBe IntPair(3, 3)

          result.targetReached shouldBe false
        }

        it("should find the fastest path - test 3") {
          // If
          graph = SimpleMockGraphMap(0, 0, 4, 4, 16)
          val world =
              Matrix(
                  gdxArrayOf(
                      gdxArrayOf("S", "0", "!", "T"),
                      gdxArrayOf("!", "0", "!", "0"),
                      gdxArrayOf("0", "0", "!", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                  ))

          val params = createPathfinderParams(world, false)
          val pathfinder = Pathfinder(graph, params)

          // when
          val result = pathfinder.call()
          val graphPath = result.graphPath

          // then
          graphPath shouldNotBe null
          graphPath!!.size shouldBe 10

          graphPath[9] shouldBe IntPair(0, 3)
          graphPath[8] shouldBe IntPair(1, 3)
          graphPath[7] shouldBe IntPair(1, 2)
          graphPath[6] shouldBe IntPair(1, 1)
          graphPath[5] shouldBe IntPair(1, 0)
          graphPath[4] shouldBe IntPair(2, 0)
          graphPath[3] shouldBe IntPair(3, 0)
          graphPath[2] shouldBe IntPair(3, 1)
          graphPath[1] shouldBe IntPair(3, 2)
          graphPath[0] shouldBe IntPair(3, 3)

          result.targetReached shouldBe false
        }

        it("should find the fastest path - test 4") {
          // If
          graph = SimpleMockGraphMap(0, 0, 4, 4, 16)
          val world =
              Matrix(
                  gdxArrayOf(
                      gdxArrayOf("S", "0", "!", "T"),
                      gdxArrayOf("!", "0", "!", "0"),
                      gdxArrayOf("0", "0", "!", "0"),
                      gdxArrayOf("0", "0", "!", "0"),
                  ))

          val params = createPathfinderParams(world, true)
          val pathfinder = Pathfinder(graph, params)

          // when
          val result = pathfinder.call()

          val graphPath = result.graphPath

          // then
          graphPath shouldBe null
          result.targetReached shouldBe false
        }

        it("should find the fastest path - test 5") {
          // If
          graph = SimpleMockGraphMap(0, 0, 4, 4, 16)

          val world =
              Matrix(
                  gdxArrayOf(
                      gdxArrayOf("X", "0", "0", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                      gdxArrayOf("0", "0", "0", "0"),
                  ))

          val params = createPathfinderParams(world, true)
          val pathfinder = Pathfinder(graph, params)

          // when
          val result = pathfinder.call()

          // then
          result.graphPath shouldBe null
          result.worldPath shouldBe null
          result.targetReached shouldBe true
        }
      }
    })
