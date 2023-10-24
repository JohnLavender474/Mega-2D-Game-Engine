package com.engine.graph

import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.IGameShape2DSupplier
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class MockObject(val id: Int, val rectangle: GameRectangle) : IGameShape2DSupplier {
  override fun getGameShape2D() = rectangle

  fun overlaps(other: Rectangle) = other.overlaps(rectangle)

  override fun equals(other: Any?) = other is MockObject && other.id == id

  override fun hashCode() = id
}

class SimpleNodeGraphMapTest :
    DescribeSpec({
      describe("SimpleNodeGraphMap") {
        it("should add objects to the correct cells") {
          // if
          val width = 100
          val height = 100
          val ppm = 10
          val graphMap = SimpleNodeGraphMap(0, 0, width, height, ppm)

          val objects =
              listOf(
                  MockObject(1, GameRectangle(10, 10, 10, 10)),
                  MockObject(2, GameRectangle(40, 40, 20, 20)),
                  MockObject(3, GameRectangle(500, 500, 5, 5)))

          // when
          objects.forEach { graphMap.add(it) }

          // then
          for (x in 0 until width) {
            for (y in 0 until height) {
              val bounds = GameRectangle(x.toFloat() * ppm, y.toFloat() * ppm, ppm, ppm)
              val cellObjects = graphMap.get(x, y)

              objects.filter { it.overlaps(bounds) }.forEach { cellObjects shouldContain it }
            }
          }
        }

        it("should retrieve objects in the specified area") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val graphMap = SimpleNodeGraphMap(0, 0, width, height, ppm)

          val objects =
              listOf(
                  MockObject(1, GameRectangle(10, 10, 20, 20)),
                  MockObject(2, GameRectangle(40, 40, 10, 10)),
                  MockObject(3, GameRectangle(80, 80, 5, 5)))

          objects.forEach { graphMap.add(it) }

          val minX = 0
          val minY = 0
          val maxX = 30
          val maxY = 30

          // when
          val retrievedObjects = graphMap.get(minX, minY, maxX, maxY)

          // then
          retrievedObjects shouldContain objects[0]
        }

        it("should reset the graph map") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val graphMap = SimpleNodeGraphMap(0, 0, width, height, ppm)

          val objects =
              listOf(
                  MockObject(1, GameRectangle(10, 10, 20, 20)),
                  MockObject(2, GameRectangle(40, 40, 10, 10)),
                  MockObject(3, GameRectangle(80, 80, 5, 5)))

          objects.forEach { graphMap.add(it) }

          // when
          graphMap.reset()

          // then
          for (x in 0 until width) {
            for (y in 0 until height) {
              val cellObjects = graphMap.get(x, y)
              cellObjects.isEmpty shouldBe true
            }
          }
        }
      }
    })
