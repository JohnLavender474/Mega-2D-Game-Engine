package com.engine.graph

import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.GameShape2D
import com.engine.world.Body
import com.engine.world.BodyType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class QuadTreeGraphMapTest :
    DescribeSpec({
      describe("QuadTreeGraphMap") {
        it("should add objects to the correct cells") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val depth = 4
          val quadTreeGraphMap = QuadTreeGraphMap(width, height, ppm, depth)
          val objects =
              listOf(
                  Body(BodyType.ABSTRACT, 10f, 10f, 20f, 20f),
                  Body(BodyType.ABSTRACT, 40f, 40f, 10f, 10f),
                  Body(BodyType.ABSTRACT, 80f, 80f, 5f, 5f))

          // when
          objects.forEach { quadTreeGraphMap.add(it) }

          // then
          for (x in 0 until width) {
            for (y in 0 until height) {
              val cellObjects = quadTreeGraphMap.get(x, y)
              objects
                  .filter {
                    it.overlaps(GameRectangle(x.toFloat(), y.toFloat(), 1f, 1f) as GameShape2D)
                  }
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

          val quadTreeGraphMap = QuadTreeGraphMap(width, height, ppm, depth)

          val objects =
              listOf(
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
          objects
              .filter {
                it.overlaps(
                    GameRectangle(minX.toFloat(), minY.toFloat(), maxX.toFloat(), maxY.toFloat())
                        as GameShape2D)
              }
              .forEach { retrievedObjects shouldContain it }
        }

        it("should reset the graph map") {
          // if
          val width = 100
          val height = 100
          val ppm = 1
          val depth = 4

          val quadTreeGraphMap = QuadTreeGraphMap(width, height, ppm, depth)

          val objects =
              listOf(
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
