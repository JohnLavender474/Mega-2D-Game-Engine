package com.engine.common.shapes

import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GameRectangleTest :
    DescribeSpec({
      describe("GameRectangle class") {
        var gameRectangle = GameRectangle(1f, 2f, 3f, 4f)

        beforeEach { gameRectangle = GameRectangle(1f, 2f, 3f, 4f) }

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
      }
    })
