package com.engine.common.shapes

import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Direction
import com.engine.common.enums.Position
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GameRectangleTest :
    DescribeSpec({
        describe("GameRectangle class") {
            var gameRectangle = GameRectangle(1f, 2f, 3f, 4f)

            beforeEach { gameRectangle = GameRectangle(1f, 2f, 3f, 4f) }

            it("should get the position values") {
                gameRectangle.getX() shouldBe 1f
                gameRectangle.getY() shouldBe 2f
                gameRectangle.getMaxX() shouldBe 4f
                gameRectangle.getMaxY() shouldBe 6f
            }

            it("should get the center") { gameRectangle.getCenter() shouldBe Vector2(2.5f, 4f) }

            it("should set the center") {
                gameRectangle.setCenter(5f, 6f)
                gameRectangle.x shouldBe 3.5f
                gameRectangle.y shouldBe 4f
            }

            it("should get the maximum X value") { gameRectangle.getMaxX() shouldBe 4f }

            it("should get the maximum Y value") { gameRectangle.getMaxY() shouldBe 6f }

            it("should position on a point") {
                Position.values().forEach {
                    gameRectangle.positionOnPoint(Vector2(7f, 8f), it)
                    gameRectangle.getPositionPoint(it) shouldBe Vector2(7f, 8f)
                }
            }

            it("should get position point") {
                gameRectangle.getPositionPoint(Position.CENTER) shouldBe Vector2(2.5f, 4f)
            }

            it("should get the bounding rectangle") {
                val boundingRectangle = gameRectangle.getBoundingRectangle()
                boundingRectangle.x shouldBe 1f
                boundingRectangle.y shouldBe 2f
                boundingRectangle.width shouldBe 3f
                boundingRectangle.height shouldBe 4f
            }

            it("should overlap circle") {
                val circle = GameCircle(2f, 3f, 1f)
                gameRectangle.overlaps(circle) shouldBe true
            }

            it("should not overlap circle") {
                val circle = GameCircle(10f, 10f, 1f)
                gameRectangle.overlaps(circle) shouldBe false
            }

            it("should overlap line") {
                val line = GameLine(2f, 3f, 4f, 5f)
                gameRectangle.overlaps(line) shouldBe true
            }

            it("should not overlap line") {
                val line = GameLine(10f, 10f, 12f, 12f)
                gameRectangle.overlaps(line) shouldBe false
            }

            describe("should rotate correctly") {
                it("rotation test 1") {
                    val actualRectangle =
                        GameRectangle(1f, 2f, 3f, 4f).getCardinallyRotatedShape(Direction.LEFT, false)
                    val expectedRectangle = GameRectangle(-6f, 1f, 4f, 3f)
                    actualRectangle shouldBe expectedRectangle
                }

                it("rotation test 2") {
                    val actualRectangle =
                        GameRectangle(1f, 2f, 3f, 4f).getCardinallyRotatedShape(Direction.DOWN, false)
                    val expectedRectangle = GameRectangle(-4f, -6f, 3f, 4f)
                    actualRectangle shouldBe expectedRectangle
                }

                it("rotation test 3") {
                    val actualRectangle =
                        GameRectangle(1f, 2f, 3f, 4f).getCardinallyRotatedShape(Direction.RIGHT, false)
                    val expectedRectangle = GameRectangle(2f, -4f, 4f, 3f)
                    actualRectangle shouldBe expectedRectangle
                }

                it("rotation test 4") {
                    val actualRectangle =
                        GameRectangle(1f, 2f, 3f, 4f).getCardinallyRotatedShape(Direction.UP, false)
                    val expectedRectangle = GameRectangle(1f, 2f, 3f, 4f)
                    actualRectangle shouldBe expectedRectangle
                }

                it("rotation test 5") {
                    var actualRectangle = GameRectangle(1f, 2f, 3f, 4f)
                    actualRectangle.originX = 1.5f
                    actualRectangle = actualRectangle.getCardinallyRotatedShape(Direction.LEFT, false)

                    val expectedRectangle = GameRectangle(-4.5f, -0.5f, 4f, 3f)

                    actualRectangle shouldBe expectedRectangle
                }

                it("rotation test 6") {
                    var actualRectangle = GameRectangle(1f, 2f, 3f, 4f)
                    actualRectangle.originY = 3.5f
                    actualRectangle = actualRectangle.getCardinallyRotatedShape(Direction.LEFT, false)

                    val expectedRectangle = GameRectangle(-2.5f, 4.5f, 4f, 3f)

                    actualRectangle shouldBe expectedRectangle
                }

                it("rotation test 7") {
                    var actualRectangle = GameRectangle(1f, 2f, 3f, 4f)
                    actualRectangle.originX = -1.5f
                    actualRectangle.originY = 3.5f
                    actualRectangle = actualRectangle.getCardinallyRotatedShape(Direction.LEFT, false)

                    val expectedRectangle = GameRectangle(-4f, 6f, 4f, 3f)

                    actualRectangle shouldBe expectedRectangle
                }
            }
        }
    })
