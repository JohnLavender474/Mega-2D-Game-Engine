package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.Line
import com.engine.common.shapes.extensions.RectangleExtensions.getMaxX
import com.engine.common.shapes.extensions.RectangleExtensions.getMaxY
import com.engine.common.shapes.extensions.RectangleExtensions.getPositionPoint
import com.engine.common.shapes.extensions.RectangleExtensions.overlaps
import com.engine.common.shapes.extensions.RectangleExtensions.positionOnPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setCenterX
import com.engine.common.shapes.extensions.RectangleExtensions.setCenterY
import com.engine.common.shapes.extensions.RectangleExtensions.setMaxX
import com.engine.common.shapes.extensions.RectangleExtensions.setMaxY
import com.engine.common.shapes.extensions.RectangleExtensions.translate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RectangleExtensionsSpec :
    DescribeSpec({
      describe("Rectangle") {
        it("setMaxX") {
          val r = Rectangle(50f, 0f, 50f, 50f)
          r.setMaxX(0f)
          r.x shouldBe -50f
        }

        it("setMaxY") {
          val r = Rectangle(0f, 50f, 50f, 50f)
          r.setMaxY(0f)
          r.y shouldBe -50f
        }

        it("getMaxX") {
          val r = Rectangle(0f, 0f, 50f, 100f)
          r.getMaxX() shouldBe 50f
        }

        it("getMaxY") {
          val r = Rectangle(0f, 0f, 50f, 100f)
          r.getMaxY() shouldBe 100f
        }

        it("setCenterX") {
          val r = Rectangle(50f, 50f, 100f, 100f)
          r.setCenterX(0f)
          r.x + r.width / 2f shouldBe 0f
        }

        it("setCenterY") {
          val r = Rectangle(50f, 50f, 100f, 100f)
          r.setCenterY(0f)
          r.y + r.height / 2f shouldBe 0f
        }

        it("translate") {
          val r = Rectangle(0f, 0f, 50f, 50f)
          r.translate(5f, 10f)
          r.x shouldBe 5f
          r.y shouldBe 10f
        }

        describe("overlaps") {
          it("overlaps Rectangle") {
            val r1 = Rectangle(0f, 0f, 50f, 50f)
            val r2 = Rectangle(25f, 25f, 75f, 75f)
            r1.overlaps(r2) shouldBe true
          }

          it("overlaps Circle") {
            val r = Rectangle(0f, 0f, 50f, 50f)
            val c = Circle(0f, 0f, 25f)
            r.overlaps(c) shouldBe true
          }

          it("overlaps Line") {
            val r = Rectangle(0f, 0f, 50f, 50f)
            val l = Line(-25f, -25f, 25f, 25f)
            r.overlaps(l) shouldBe true
          }

          it("overlaps Polyline") {
            val r = Rectangle(0f, 0f, 50f, 50f)
            val p = Polyline(floatArrayOf(0f, 0f, 10f, 10f, -20f, 20f))
            r.overlaps(p) shouldBe true
          }
        }

        it("positionOnPoint") {
          val r = Rectangle(0f, 0f, 50f, 50f)
          Position.values().forEach {
            r.positionOnPoint(Vector2.Zero, it)
            r.getPositionPoint(it) shouldBe Vector2.Zero
          }
        }
      }
    })
