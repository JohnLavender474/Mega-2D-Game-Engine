package com.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LineSpec :
    DescribeSpec({
      describe("Line") {
        it("getCenter") {
          val l = Line(0f, 0f, 50f, 50f)
          l.getCenter() shouldBe Vector2(25f, 25f)
        }

        it("setCenterX") {
          val l = Line(0f, 0f, 50f, 50f)
          l.setCenterX(0f)
          l.getCenter() shouldBe Vector2(0f, 25f)
        }

        it("setCenterY") {
          val l = Line(0f, 0f, 50f, 50f)
          l.setCenterY(0f)
          l.getCenter() shouldBe Vector2(25f, 0f)
        }

        it("getMaxX") {
          val l = Line(0f, 0f, 50f, 50f)
          l.getMaxX() shouldBe 50f
        }

        it("getMaxY") {
          val l = Line(0f, 0f, 50f, 50f)
          l.getMaxY() shouldBe 50f
        }

        it("setMaxX") {
          val l = Line(0f, 0f, 50f, 50f)
          l.setMaxX(0f)
          l.getMaxX() shouldBe 0f
        }

        it("setMaxY") {
          val l = Line(0f, 0f, 50f, 50f)
          l.setMaxY(0f)
          l.getMaxY() shouldBe 0f
        }

        describe("overlaps") {
          it("overlaps Rectangle") {
            val l = Line(0f, 0f, 50f, 50f)
            val r = Rectangle(0f, 0f, 50f, 50f)
            l.overlaps(r) shouldBe true
          }

          it("overlaps Circle") {
            val l = Line(0f, 0f, 50f, 50f)
            val c = Circle(0f, 0f, 50f)
            l.overlaps(c) shouldBe true
          }

          it("overlaps Line") {
            val l1 = Line(0f, 0f, 50f, 50f)
            val l2 = Line(0f, 50f, 50f, 0f)
            l1.overlaps(l2) shouldBe true
          }

          it("overlaps Polyline") {
            val l = Line(0f, 0f, 50f, 50f)
            val c = Circle(0f, 0f, 50f)
            l.overlaps(c) shouldBe true
          }
        }
      }
    })
