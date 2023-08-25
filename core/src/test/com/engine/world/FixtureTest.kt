package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.GameShape2D
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class FixtureTest :
    DescribeSpec({
      describe("Fixture class") {
        val mockShape = mockk<GameShape2D>()
        val fixtureType = "type"
        val offset = Vector2(5f, 5f)
        val userData = hashMapOf<String, Any?>("key" to "value")

        val fixture =
            Fixture(mockShape, fixtureType, offsetFromBodyCenter = offset, userData = userData)

        it("should have the correct initial properties") {
          fixture.shape shouldBe mockShape
          fixture.fixtureType shouldBe fixtureType
          fixture.active shouldBe true
          fixture.attachedToBody shouldBe true
          fixture.offsetFromBodyCenter shouldBe offset
          fixture.userData shouldBe userData
          fixture.getUserData("key", String::class) shouldBe "value"
          fixture.setUserData("hello", 1)
          fixture.getUserData("hello", Int::class) shouldBe 1
          fixture.setUserData("world", null)
          fixture.getUserData("world") shouldBe null
        }

        it("should overlap with another fixture") {
          every { mockShape.overlaps(any()) } returns true
          val otherFixture = Fixture(mockShape, "otherType")
          fixture.overlaps(otherFixture) shouldBe true
          verify { mockShape.overlaps(fixture.shape) }
        }

        it("should overlap with a shape") {
          val otherShape = mockk<GameShape2D>()
          every { mockShape.overlaps(any()) } returns true
          fixture.overlaps(otherShape) shouldBe true
          verify { mockShape.overlaps(fixture.shape) }
        }
      }
    })
