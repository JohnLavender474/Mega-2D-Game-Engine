package com.mega.game.engine.world.body

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class FixtureTest :
    DescribeSpec({
        describe("Fixture class") {
            val shape = GameRectangle(0f, 0f, 10f, 10f)
            val fixtureLabel = "type"
            val offset = Vector2(5f, 5f)
            val body = Body(BodyType.DYNAMIC)

            val fixture = Fixture(body, fixtureLabel, shape, offsetFromBodyCenter = offset)

            it("should have the correct initial properties") {
                fixture.rawShape shouldBe shape
                fixture.type shouldBe fixtureLabel
                fixture.active shouldBe true
                fixture.attachedToBody shouldBe true
                fixture.offsetFromBodyCenter shouldBe offset
            }

            it("should overlap with another fixture") {
                val otherFixture = Fixture(body, "otherType", shape)
                fixture.overlaps(otherFixture) shouldBe true
            }

            it("should overlap with a shape") {
                fixture.overlaps(shape) shouldBe true
            }

            it("should return the correct shape") {
                val bodyCenter = body.getBodyBounds().getCenter()
                val expectedShape = shape.copy().setCenter(bodyCenter).translation(offset)
                val actualShape = fixture.getShape()

                actualShape shouldBe expectedShape
            }
        }
    })
