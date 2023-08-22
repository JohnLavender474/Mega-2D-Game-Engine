package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.Line
import com.engine.common.shapes.extensions.PolylineExtensions.getCenter
import com.engine.common.shapes.extensions.PolylineExtensions.getMaxX
import com.engine.common.shapes.extensions.PolylineExtensions.getMaxY
import com.engine.common.shapes.extensions.PolylineExtensions.getPositionPoint
import com.engine.common.shapes.extensions.PolylineExtensions.overlaps
import com.engine.common.shapes.extensions.PolylineExtensions.positionOnPoint
import com.engine.common.shapes.extensions.PolylineExtensions.setCenterX
import com.engine.common.shapes.extensions.PolylineExtensions.setCenterY
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PolylineExtensionsSpec :
    DescribeSpec({
      describe("Polyline") {
        it("getCenter") {
          val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.getCenter() shouldBe Vector2(50f, 25f)
        }

        it("setCenterX") {
          val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.setCenterX(0f)
          p.getCenter().x shouldBe 0f
        }

        it("setCenterY") {
          val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.setCenterY(0f)
          p.getCenter().y shouldBe 0f
        }

        it("getMaxX") {
          val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.getMaxX() shouldBe 100f
        }

        it("getMaxY") {
          val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
          p.getMaxY() shouldBe 50f
        }

        describe("overlaps") {
          it("overlaps Rectangle") {
            val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val r = Rectangle(0f, 0f, 50f, 50f)
            p.overlaps(r) shouldBe true
          }

          it("overlaps Circle") {
            val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val c = Circle(0f, 0f, 50f)
            p.overlaps(c) shouldBe true
          }

          it("overlaps Line") {
            val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val l = Line(0f, 0f, 50f, 50f)
            p.overlaps(l) shouldBe true
          }

          it("overlaps Polyline") {
            val p1 = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
            val p2 = Polyline(floatArrayOf(-25f, -25f, -10f, -10f, 25f, 25f, 50f, -25f))
            p1.overlaps(p2) shouldBe true
          }
        }

        it("positionOnPoint") {
          val p = Polyline(floatArrayOf(-10f, -25f, 50f, 75f, 45f, -100f))
          Position.values().forEach {
            p.positionOnPoint(Vector2.Zero, it)
            p.getPositionPoint(it) shouldBe Vector2.Zero
          }
        }
      }
    })
