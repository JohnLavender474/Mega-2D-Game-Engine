package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.IGameShape2D
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class FixtureTest :
    DescribeSpec({
      describe("Fixture class") {
        val mockShape = mockk<IGameShape2D>()
        val fixtureLabel = "type"
        val offset = Vector2(5f, 5f)

        val fixture =
            Fixture(mockShape, fixtureLabel, offsetFromBodyCenter = offset)

        it("should have the correct initial properties") {
          fixture.shape shouldBe mockShape
          fixture.fixtureLabel shouldBe fixtureLabel
          fixture.active shouldBe true
          fixture.attachedToBody shouldBe true
          fixture.offsetFromBodyCenter shouldBe offset
        }

        it("should overlap with another fixture") {
          every { mockShape.overlaps(any()) } returns true
          val otherFixture = Fixture(mockShape, "otherType")
          fixture.overlaps(otherFixture) shouldBe true
          verify { mockShape.overlaps(fixture.shape) }
        }

        it("should overlap with a shape") {
          val otherShape = mockk<IGameShape2D>()
          every { mockShape.overlaps(any()) } returns true
          fixture.overlaps(otherShape) shouldBe true
          verify { mockShape.overlaps(fixture.shape) }
        }
      }
    })
