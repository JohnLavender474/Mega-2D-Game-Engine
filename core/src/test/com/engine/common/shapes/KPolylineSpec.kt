package com.engine.common.shapes

import com.badlogic.gdx.math.Vector2
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class KPolylineSpec :
    DescribeSpec({
      describe("KPolyline") {
        it("getCenter") {
          val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.getCenter() shouldBe Vector2(50f, 25f)
        }

        it("setCenterX") {
          val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.setCenterX(0f)
          p.getCenter().x shouldBe 0f
        }

        it("setCenterY") {
          val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.setCenterY(0f)
          p.getCenter().y shouldBe 0f
        }

        it("getMaxX") {
          val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.getMaxX() shouldBe 100f
        }

        it("getMaxY") {
          val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.getMaxY() shouldBe 50f
        }

        describe("overlaps") {
          it("overlaps KRectangle") {
            val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val r = KRectangle(0f, 0f, 50f, 50f)
            p.overlaps(r) shouldBe true
          }

          it("overlaps KCircle") {
            val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val c = KCircle(0f, 0f, 50f)
            p.overlaps(c) shouldBe true
          }

          it("overlaps KLine") {
            val p = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val l = KLine(0f, 0f, 50f, 50f)
            p.overlaps(l) shouldBe true
          }

          it("overlaps KPolyline") {
            val p1 = KPolyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val p2 = KPolyline(floatArrayOf(-25f, -25f, -10f, -10f, 25f, 25f, 50f, -25f))
            p1.overlaps(p2) shouldBe true
          }
        }
      }
    })
