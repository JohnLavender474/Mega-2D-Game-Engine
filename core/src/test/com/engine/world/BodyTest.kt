package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.enums.Direction
import com.engine.common.extensions.gdxArrayOf
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*

class BodyTest :
    DescribeSpec({
        describe("Body") {
            val bodyType = BodyType.ABSTRACT
            val physicsData = PhysicsData()
            val fixtures = gdxArrayOf<Pair<Any, Fixture>>()
            val props = Properties()
            val body = Body(bodyType, physicsData, fixtures, properties = props)

            it("should have the correct initial properties") {
                body.bodyType shouldBe bodyType
                body.physics shouldBe physicsData
                body.fixtures shouldBe fixtures
                body.properties shouldBe props
                body.preProcess shouldBe OrderedMap<Any, Updatable>()
                body.postProcess shouldBe OrderedMap<Any, Updatable>()
            }

            it("should have the correct default values for optional properties") {
                body.x shouldBe 0f
                body.y shouldBe 0f
                body.width shouldBe 0f
                body.height shouldBe 0f
                body.previousBounds shouldBe GameRectangle()
                body.hashCode() shouldBe System.identityHashCode(body)
            }

            it("should return previous bounds correctly") {
                val previousBounds = body.getPreviousBounds()
                previousBounds shouldBe GameRectangle()
            }

            it("should check if it has a given body type correctly") {
                body.isBodyType(bodyType) shouldBe true
            }

            it("should get user data correctly") {
                val key = "key"
                val value = "value"
                body.putProperty(key, value)
                val propsValue = body.getProperty(key, String::class)
                propsValue shouldBe value
            }

            it("should reset correctly") {
                val mockPhysicsData = spyk<PhysicsData>()
                every { mockPhysicsData.reset() } just Runs
                body.physics = mockPhysicsData
                body.reset()
                verify { body.physics.reset() }
            }

            it("should have proper equals and hashCode implementations") {
                val body1 = Body(bodyType, physicsData, fixtures, props)
                val body2 = Body(bodyType, physicsData, fixtures, props)
                (body1 == body2) shouldBe false
                body1.hashCode() shouldBe System.identityHashCode(body1)
                body2.hashCode() shouldBe System.identityHashCode(body2)
                body1.hashCode() shouldNotBe body2.hashCode()
            }

            it("should set fixtures relative to body") {
                body.setSize(10f)
                body.setCenter(0f, 0f)

                val bottomFixture = Fixture(GameRectangle().setSize(1f), "bottom")
                bottomFixture.offsetFromBodyCenter.y = -5f
                body.addFixture(bottomFixture)

                val topFixture = Fixture(GameRectangle().setSize(1f), "top")
                topFixture.offsetFromBodyCenter.y = 5f
                body.addFixture(topFixture)

                val leftFixture = Fixture(GameRectangle().setSize(1f), "left")
                leftFixture.offsetFromBodyCenter.x = -5f
                body.addFixture(leftFixture)

                val rightFixture = Fixture(GameRectangle().setSize(1f), "right")
                rightFixture.offsetFromBodyCenter.x = 5f
                body.addFixture(rightFixture)

                body.cardinalRotation = Direction.UP
                body.forEachFixture { _, fixture -> fixture.setBodyRelativeShape(body) }

                (bottomFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, -5f)
                (topFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, 5f)
                (leftFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(-5f, 0f)
                (rightFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(5f, 0f)

                body.cardinalRotation = Direction.LEFT
                body.forEachFixture { _, fixture -> fixture.setBodyRelativeShape(body) }

                (bottomFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(5f, 0f)
                (topFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(-5f, 0f)
                (leftFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, -5f)
                (rightFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, 5f)

                body.cardinalRotation = Direction.DOWN
                body.forEachFixture { _, fixture -> fixture.setBodyRelativeShape(body) }

                (bottomFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, 5f)
                (topFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, -5f)
                (leftFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(5f, 0f)
                (rightFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(-5f, 0f)

                body.cardinalRotation = Direction.RIGHT
                body.forEachFixture { _, fixture -> fixture.setBodyRelativeShape(body) }

                (bottomFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(-5f, 0f)
                (topFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(5f, 0f)
                (leftFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, 5f)
                (rightFixture.bodyRelativeShape as GameRectangle).getCenter() shouldBe Vector2(0f, -5f)
            }
        }
    })
