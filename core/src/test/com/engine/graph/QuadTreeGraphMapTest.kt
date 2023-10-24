package com.engine.graph

import com.engine.common.extensions.gdxArrayOf
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.IGameShape2D
import com.engine.world.Body
import com.engine.world.BodyType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class QuadTreeGraphMapTest :
    DescribeSpec({
      describe("QuadTreeGraphMap") {
        it("should not add out-of-bounds object") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val depth = 4

          val quadTreeGraphMap = QuadTreeGraphMap(0, 0, width, height, ppm, depth)
          val body = Body(BodyType.ABSTRACT, 110f, 110f, 10f, 10f)

          // when
          val result = quadTreeGraphMap.add(body)

          // then
          result shouldBe false
        }

        it("should add objects to the correct cells") {
          // if
          val width = 100
          val height = 100
          val ppm = 10
          val depth = 10
          val quadTreeGraphMap = QuadTreeGraphMap(0, 0, width, height, ppm, depth)

          val bodies =
              gdxArrayOf(
                  Body(BodyType.ABSTRACT, 10f, 10f, 10f, 10f),
                  Body(BodyType.ABSTRACT, 40f, 40f, 20f, 20f),
                  Body(BodyType.ABSTRACT, 500f, 500f, 5f, 5f))

          // when
          quadTreeGraphMap.addAll(bodies)

          // then
          for (x in 0 until width) {
            for (y in 0 until height) {
              val rectangle = GameRectangle(x.toFloat() * ppm, y.toFloat() * ppm, ppm, ppm)
              val cellObjects = quadTreeGraphMap.get(x, y)

              bodies
                  .filter { it.overlaps(rectangle as IGameShape2D) }
                  .forEach { cellObjects shouldContain it }
            }
          }
        }

        it("should retrieve objects in the specified area") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val depth = 4

          val quadTreeGraphMap = QuadTreeGraphMap(0, 0, width, height, ppm, depth)

          val objects =
              gdxArrayOf(
                  Body(BodyType.ABSTRACT, 10f, 10f, 20f, 20f),
                  Body(BodyType.ABSTRACT, 40f, 40f, 10f, 10f),
                  Body(BodyType.ABSTRACT, 80f, 80f, 5f, 5f))

          // when
          objects.forEach { quadTreeGraphMap.add(it) }

          val minX = 0
          val minY = 0
          val maxX = 30
          val maxY = 30

          val retrievedObjects = quadTreeGraphMap.get(minX, minY, maxX, maxY)

          // then
          retrievedObjects.size shouldBe 1
          retrievedObjects shouldContain objects[0]
        }

        it("should reset the graph map") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val depth = 4

          val quadTreeGraphMap = QuadTreeGraphMap(0, 0, width, height, ppm, depth)

          val objects =
              gdxArrayOf(
                  Body(BodyType.ABSTRACT, 10f, 10f, 20f, 20f),
                  Body(BodyType.ABSTRACT, 40f, 40f, 10f, 10f),
                  Body(BodyType.ABSTRACT, 80f, 80f, 5f, 5f))

          // when
          objects.forEach { quadTreeGraphMap.add(it) }

          quadTreeGraphMap.reset()

          // then
          for (x in 0 until width) {
            for (y in 0 until height) {
              val cellObjects = quadTreeGraphMap.get(x, y)
              cellObjects.isEmpty() shouldBe true
            }
          }
        }
      }
    })
