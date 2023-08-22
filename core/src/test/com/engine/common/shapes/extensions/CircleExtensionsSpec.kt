package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.Line
import com.engine.common.shapes.extensions.CircleExtensions.getCenter
import com.engine.common.shapes.extensions.CircleExtensions.getMaxX
import com.engine.common.shapes.extensions.CircleExtensions.getMaxY
import com.engine.common.shapes.extensions.CircleExtensions.getPositionPoint
import com.engine.common.shapes.extensions.CircleExtensions.positionOnPoint
import com.engine.common.shapes.extensions.CircleExtensions.setCenterX
import com.engine.common.shapes.extensions.CircleExtensions.setCenterY
import com.engine.common.shapes.extensions.CircleExtensions.overlaps
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CircleSpec: DescribeSpec({
    describe("Circle") {
        it("getCenter") {
            val c = Circle(50f, 50f, 50f)
            c.getCenter() shouldBe Vector2(50f, 50f)
        }

        it("setCenterX") {
            val c = Circle(50f, 50f, 50f)
            c.setCenterX(0f)
            c.getCenter() shouldBe Vector2(0f, 50f)
        }

        it("setCenterY") {
            val c = Circle(50f, 50f, 50f)
            c.setCenterY(0f)
            c.getCenter() shouldBe Vector2(50f, 0f)
        }

        it("getMaxX") {
            val c = Circle(50f, 50f, 50f)
            c.getMaxX() shouldBe 100f
        }

        it("getMaxY") {
            val c = Circle(50f, 50f, 50f)
            c.getMaxY() shouldBe 100f
        }

        describe("overlaps") {
            it("overlaps Rectangle") {
                val c = Circle(50f, 50f, 50f)
                val r = Rectangle(0f, 0f, 50f, 50f)
                c.overlaps(r) shouldBe true
            }

            it("overlaps Circle") {
                val c1 = Circle(50f, 50f, 50f)
                val c2 = Circle(0f, 0f, 50f)
                c1.overlaps(c2) shouldBe true
            }

            it("overlaps Line") {
                val c = Circle(50f, 50f, 50f)
                val l = Line(0f, 0f, 50f, 50f)
                c.overlaps(l) shouldBe true
            }

            it("overlaps Polyline") {
                val c = Circle(0f, 0f, 50f)
                val p = Polyline(floatArrayOf(0f, 0f, 50f, 50f, 100f, 0f))
                c.overlaps(p) shouldBe true
            }
        }

        it("positionOnPoint") {
            val c = Circle(50f, 50f, 50f)
            Position.values().forEach {
                c.positionOnPoint(Vector2.Zero, it)
                c.getPositionPoint(it) shouldBe Vector2.Zero
            }
        }
    }

})