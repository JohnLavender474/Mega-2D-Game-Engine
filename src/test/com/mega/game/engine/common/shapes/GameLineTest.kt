package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.getRandom
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GameLineTest :
    DescribeSpec({
        describe("GameLine class") {
            it("should construct a line with specified points") {
                val line = GameLine(0f, 0f, 3f, 4f)
                line.getLength() shouldBe 5f
                line.getMaxX() shouldBe 3f
                line.getMaxY() shouldBe 4f
            }

            it("should calculate line length correctly") {
                val line = GameLine(1f, 2f, 4f, 6f)
                line.getLength() shouldBe 5f
            }

            it("should correctly check containment of a point") {
                val line = GameLine(1f, 1f, 4f, 4f)
                val pointInside = Vector2(2f, 2f)
                val pointOutside = Vector2(5f, 5f)
                line.contains(pointInside) shouldBe true
                line.contains(pointOutside) shouldBe false
            }

            it("should correctly check containment using coordinates") {
                val line = GameLine(1f, 1f, 4f, 4f)
                val xInside = 2f
                val yInside = 2f
                val xOutside = 5f
                val yOutside = 5f
                line.contains(xInside, yInside) shouldBe true
                line.contains(xOutside, yOutside) shouldBe false
            }

            it("should calculate the center") {
                val line = GameLine(1f, 1f, 4f, 4f)
                val expectedCenter = Vector2(2.5f, 2.5f)
                line.getCenter() shouldBe expectedCenter
            }

            it("should handle translation correctly") {
                val line = GameLine(1f, 1f, 4f, 4f)
                val translationX = 2f
                val translationY = 2f
                val expectedCenter = Vector2(4.5f, 4.5f)
                line.translation(translationX, translationY)
                line.getCenter() shouldBe expectedCenter
            }

            it("should correctly check overlaps with another line") {
                val line1 = GameLine(1f, 1f, 4f, 4f)
                val line2 = GameLine(2f, 1f, 4f, 6f)
                val line3 = GameLine(5f, 1f, 8f, 4f)

                line1.overlaps(line2) shouldBe true
                line1.overlaps(line3) shouldBe false
            }

            it("should provide correct local and world points") {
                for (i in 0 until 10) {

                    val random = Array<Float>()
                    for (j in 0 until 9) random.add(getRandom(0, 359).toFloat())

                    // control line
                    val controlLine = Polyline()
                    controlLine.setVertices(floatArrayOf(random[0], random[1], random[2], random[3]))
                    controlLine.setPosition(random[4], random[5])
                    controlLine.setOrigin(random[6], random[7])
                    controlLine.rotation = random[8]

                    // test line
                    val testLine = GameLine(random[0], random[1], random[2], random[3])
                    testLine.setPosition(random[4], random[5])
                    testLine.setOrigin(random[6], random[7])
                    testLine.rotation = random[8]

                    // test local points
                    val controlLocalPoints = controlLine.vertices
                    println("Control local points: ${controlLocalPoints.contentToString()}")
                    val testLocalPoints = testLine.getLocalPoints()
                    println("Test local points: $testLocalPoints")
                    testLocalPoints.first shouldBe Vector2(controlLocalPoints[0], controlLocalPoints[1])
                    testLocalPoints.second shouldBe Vector2(controlLocalPoints[2], controlLocalPoints[3])

                    // test world points
                    val controlWorldPoints = controlLine.transformedVertices
                    println("Control world points: ${controlWorldPoints.contentToString()}")
                    val testWorldPoints = testLine.getWorldPoints()
                    println("Test world points: $testWorldPoints")
                    testWorldPoints.first shouldBe Vector2(controlWorldPoints[0], controlWorldPoints[1])
                    testWorldPoints.second shouldBe Vector2(controlWorldPoints[2], controlWorldPoints[3])
                }
            }

            it("should set the center correctly") {
                val line = GameLine(0f, 0f, 1f, 1f)
                line.setCenter(Vector2(0.5f, 0.5f))
                val center = line.getCenter()
                center shouldBe Vector2(0.5f, 0.5f)
            }

            it("should return correct local center") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val localCenter = line.getLocalCenter()
                localCenter shouldBe Vector2(0.5f, 0.5f)
            }

            it("should set X coordinate correctly") {
                val line = GameLine(0f, 0f, 1f, 1f)
                line.setX(2f)
                line.getX() shouldBe 2f
            }

            it("should set Y coordinate correctly") {
                val line = GameLine(0f, 0f, 1f, 1f)
                line.setY(2f)
                line.getY() shouldBe 2f
            }

            it("should provide correct maxX") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val maxX = line.getMaxX()
                maxX shouldBe 1f
            }

            it("should provide correct maxY") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val maxY = line.getMaxY()
                maxY shouldBe 1f
            }

            it("should translate correctly") {
                val line = GameLine(0f, 0f, 1f, 1f)
                line.translation(1f, 1f)
                val center = line.getCenter()
                center shouldBe Vector2(1.5f, 1.5f)
            }

            it("should overlap rectangle") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val rectangle = GameRectangle(0.5f, 0.5f, 1f, 1f)
                line.overlaps(rectangle) shouldBe true
            }

            it("should not overlap rectangle") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val rectangle = GameRectangle(2f, 2f, 1f, 1f)
                line.overlaps(rectangle) shouldBe false
            }

            it("should overlap circle") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val circle = GameCircle(0.5f, 0.5f, 1f)
                line.overlaps(circle) shouldBe true
            }

            it("should not overlap circle") {
                val line = GameLine(0f, 0f, 1f, 1f)
                val circle = GameCircle(3f, 3f, 1f)
                line.overlaps(circle) shouldBe false
            }
        }
    })
