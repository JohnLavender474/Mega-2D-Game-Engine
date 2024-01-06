package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class FixtureTest :
    DescribeSpec({
      describe("Fixture class") {
        val shape = GameRectangle(0f, 0f, 10f, 10f)
        val fixtureLabel = "type"
        val offset = Vector2(5f, 5f)
        val body = Body(BodyType.DYNAMIC)

        val fixture = Fixture(shape, fixtureLabel, offsetFromBodyCenter = offset)

        it("should have the correct initial properties") {
          fixture.shape shouldBe shape
          fixture.fixtureLabel shouldBe fixtureLabel
          fixture.active shouldBe true
          fixture.attachedToBody shouldBe true
          fixture.offsetFromBodyCenter shouldBe offset
        }

        it("should overlap with another fixture") {
          fixture.setBodyRelativeShape(body)
          val otherFixture = Fixture(shape, "otherType")
          otherFixture.setBodyRelativeShape(body)
          fixture.overlaps(otherFixture) shouldBe true
        }

        it("should overlap with a shape") {
          fixture.setBodyRelativeShape(body)
          fixture.overlaps(shape) shouldBe true
        }
      }
    })
