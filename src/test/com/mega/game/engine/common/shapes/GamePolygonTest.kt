package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.extensions.gdxFloatArrayOf
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GamePolygonTest : DescribeSpec({
    describe("GamePolygon class") {

        lateinit var gamePolygon: GamePolygon

        beforeEach {
            gamePolygon = GamePolygon(gdxFloatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
        }

        it("should contain the point") {
            gamePolygon.contains(Vector2(0.5f, 0.5f)) shouldBe true
        }

        it("should not contain the point") {
            gamePolygon.contains(Vector2(2f, 2f)) shouldBe false
        }

        it("should get the bounding rectangle") {
            val boundingRectangle = gamePolygon.getBoundingRectangle()
            boundingRectangle.x shouldBe 0f
            boundingRectangle.y shouldBe 0f
            boundingRectangle.width shouldBe 1f
            boundingRectangle.height shouldBe 1f
        }

        it("should overlap with another polygon") {
            val otherPolygon = GamePolygon(gdxFloatArrayOf(0.5f, 0.5f, 1.5f, 0.5f, 1.5f, 1.5f, 0.5f, 1.5f))
            gamePolygon.overlaps(otherPolygon) shouldBe true
        }

        it("should not overlap with another polygon") {
            val otherPolygon = GamePolygon(gdxFloatArrayOf(2f, 2f, 3f, 2f, 3f, 3f, 2f, 3f))
            gamePolygon.overlaps(otherPolygon) shouldBe false
        }

        it("should set and get origin") {
            gamePolygon.originX = 2.0f
            gamePolygon.originY = 2.0f
            gamePolygon.originX shouldBe 2.0f
            gamePolygon.originY shouldBe 2.0f
        }

        it("should set and get rotation") {
            gamePolygon.rotation = 45f
            gamePolygon.rotation shouldBe 45f
        }

        it("should set and get scale") {
            gamePolygon.scaleX = 2.0f
            gamePolygon.scaleY = 3.0f
            gamePolygon.scaleX shouldBe 2.0f
            gamePolygon.scaleY shouldBe 3.0f
        }

        it("should set and get vertices") {
            val vertices = gdxFloatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f)
            gamePolygon.localVertices = vertices
            gamePolygon.localVertices shouldBe vertices
        }

        it("should rotate the polygon") {
            gamePolygon.rotate(90f)
            val polygon = Polygon(floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
            polygon.rotate(90f)
            val rotatedVertices = polygon.transformedVertices
            gamePolygon.localVertices.size shouldBe rotatedVertices.size
            for (i in rotatedVertices.indices) {
                gamePolygon.transformedVertices[i] shouldBe rotatedVertices[i]
            }
        }

        it("should scale the polygon") {
            gamePolygon.scale(2.0f)
            val polygon = Polygon(floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
            polygon.scale(2.0f)
            val scaledVertices = polygon.transformedVertices
            gamePolygon.localVertices.size shouldBe scaledVertices.size
            for (i in scaledVertices.indices) {
                gamePolygon.transformedVertices[i] shouldBe scaledVertices[i]
            }
        }

        it("should calculate the area of the polygon") {
            val area = gamePolygon.area()
            val polygon = Polygon(floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
            val expectedArea = polygon.area()
            area shouldBe expectedArea
        }

        it("should get the centroid of the polygon") {
            val centroid = Vector2()
            gamePolygon.getCentroid(centroid)
            val polygon = Polygon(floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
            val expectedCentroid = Vector2()
            polygon.getCentroid(expectedCentroid)
            centroid.x shouldBe expectedCentroid.x
            centroid.y shouldBe expectedCentroid.y
        }
    }
})
