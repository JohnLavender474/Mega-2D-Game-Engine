package com.engine.common.shapes

import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class KRectangleSpec :
    DescribeSpec({
      describe("KRectangle") {
        it("setMaxX") {
          val r = KRectangle(50f, 0f, 50f, 50f)
          r.setMaxX(0f)
          r.x shouldBe -50f
        }

        it("setMaxY") {
          val r = KRectangle(0f, 50f, 50f, 50f)
          r.setMaxY(0f)
          r.y shouldBe -50f
        }

        it("getMaxX") {
          val r = KRectangle(0f, 0f, 50f, 100f)
          r.getMaxX() shouldBe 50f
        }

        it("getMaxY") {
          val r = KRectangle(0f, 0f, 50f, 100f)
          r.getMaxY() shouldBe 100f
        }

        it("setCenterX") {
          val r = KRectangle(50f, 50f, 100f, 100f)
          r.setCenterX(0f)
          r.x + r.width / 2f shouldBe 0f
        }

        it("setCenterY") {
          val r = KRectangle(50f, 50f, 100f, 100f)
          r.setCenterY(0f)
          r.y + r.height / 2f shouldBe 0f
        }

        it("translate") {
          val r = KRectangle(0f, 0f, 50f, 50f)
          r.translate(5f, 10f)
          r.x shouldBe 5f
          r.y shouldBe 10f
        }

        describe("overlaps") {
          it("overlaps KRectangle") {
            val r1 = KRectangle(0f, 0f, 50f, 50f)
            val r2 = KRectangle(25f, 25f, 75f, 75f)
            r1.overlaps(r2 as KShape2D) shouldBe true
          }

          it("overlaps KCircle") {
            val r = KRectangle(0f, 0f, 50f, 50f)
            val c = KCircle(0f, 0f, 25f)
            r.overlaps(c) shouldBe true
          }

          it("overlaps KLine") {
            val r = KRectangle(0f, 0f, 50f, 50f)
            val l = KLine(-25f, -25f, 25f, 25f)
            r.overlaps(l) shouldBe true
          }

          it("overlaps KPolyline") {
            val r = KRectangle(0f, 0f, 50f, 50f)
            val p = KPolyline(floatArrayOf(0f, 0f, 10f, 10f, -20f, 20f))
            r.overlaps(p) shouldBe true
          }
        }

        it("positionOnPoint") {
          val r = KRectangle(0f, 0f, 50f, 50f)
          Position.values().forEach {
            r.positionOnPoint(Vector2.Zero, it)
            r.getPositionPoint(it) shouldBe Vector2.Zero
          }
        }
      }
    })
