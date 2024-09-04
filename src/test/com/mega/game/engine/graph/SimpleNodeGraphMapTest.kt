package com.mega.game.engine.graph

import com.mega.game.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class SimpleNodeGraphMapTest :
    DescribeSpec({
        describe("SimpleNodeGraphMap") {
            it("should add objects to the correct cells") {
                // if
                val width = 10
                val height = 10
                val ppm = 10
                val graphMap = SimpleNodeGraphMap(0, 0, width, height, ppm)

                val objects =
                    listOf(
                        // in cell (0-1, 0-1)
                        GameRectangle(0, 0, 10, 10),
                        // in cells (4-5, 4-5)
                        GameRectangle(42, 42, 15, 15),
                        // in cells (9, 9)
                        GameRectangle(92, 92, 5, 5)
                    )

                // when
                objects.forEach { graphMap.add(it) }

                // then
                for (x in 0..width) {
                    for (y in 0..height) {
                        val cellObjects = graphMap.get(x, y)
                        println("x: $x, y: $y, cellObjects: $cellObjects")

                        if (x in 0..1 && y in 0..1) {
                            cellObjects.size shouldBe 1
                            cellObjects.contains(objects[0]) shouldBe true
                        } else if (x in 4..5 && y in 4..5) {
                            cellObjects.size shouldBe 1
                            cellObjects.contains(objects[1]) shouldBe true
                        } else if (x == 9 && y == 9) {
                            cellObjects.size shouldBe 1
                            cellObjects.contains(objects[2]) shouldBe true
                        } else {
                            cellObjects.size shouldBe 0
                        }
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
                        GameRectangle(10, 10, 20, 20),
                        GameRectangle(40, 40, 10, 10),
                        GameRectangle(80, 80, 5, 5)
                    )

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
                        GameRectangle(10, 10, 20, 20),
                        GameRectangle(40, 40, 10, 10),
                        GameRectangle(80, 80, 5, 5)
                    )

                objects.forEach { graphMap.add(it) }

                // when
                graphMap.reset()

                // then
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        val cellObjects = graphMap.get(x, y)
                        cellObjects.isEmpty() shouldBe true
                    }
                }
            }
        }
    })
