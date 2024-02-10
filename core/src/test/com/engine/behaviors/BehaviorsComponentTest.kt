package com.engine.behaviors

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class BehaviorsComponentTest :
    DescribeSpec({
        describe("BehaviorComponent") {
            val behavior1 =
                object : AbstractBehavior() {
                    override fun evaluate(delta: Float): Boolean {
                        return delta > 0.0f
                    }

                    override fun init() {}

                    override fun act(delta: Float) {}

                    override fun end() {}
                }

            val behavior2 =
                object : AbstractBehavior() {
                    override fun evaluate(delta: Float): Boolean {
                        return delta < 0.0f
                    }

                    override fun init() {}

                    override fun act(delta: Float) {}

                    override fun end() {}
                }

            val behaviorsComponent = BehaviorsComponent(mockk())

            it("should add and check behaviors") {
                behaviorsComponent.addBehavior("behavior1", behavior1)
                behaviorsComponent.addBehavior("behavior2", behavior2)

                behaviorsComponent.isBehaviorActive("behavior1") shouldBe false
                behaviorsComponent.isBehaviorActive("behavior2") shouldBe false

                behaviorsComponent.setActive("behavior1", true)
                behaviorsComponent.isBehaviorActive("behavior1") shouldBe true
            }

            it("should check if any behaviors are active") {
                behaviorsComponent.reset()

                behaviorsComponent.addBehavior("behavior1", behavior1)
                behaviorsComponent.addBehavior("behavior2", behavior2)

                behaviorsComponent.setActive("behavior1", true)
                behaviorsComponent.setActive("behavior2", false)

                behaviorsComponent.isAnyBehaviorActive(listOf("behavior1", "behavior2")) shouldBe true
                behaviorsComponent.isAnyBehaviorActive(listOf("behavior2")) shouldBe false
            }

            it("should check if all behaviors are active") {
                behaviorsComponent.reset()

                behaviorsComponent.addBehavior("behavior1", behavior1)
                behaviorsComponent.addBehavior("behavior2", behavior2)

                behaviorsComponent.setActive("behavior1", true)
                behaviorsComponent.setActive("behavior2", false)

                behaviorsComponent.areAllBehaviorsActive(listOf("behavior1", "behavior2")) shouldBe false
                behaviorsComponent.areAllBehaviorsActive(listOf("behavior1")) shouldBe true
            }

            it("should reset behaviors and active behaviors") {
                behaviorsComponent.addBehavior("behavior1", behavior1)
                behaviorsComponent.addBehavior("behavior2", behavior2)

                behaviorsComponent.setActive("behavior1", true)
                behaviorsComponent.setActive("behavior2", true)

                behaviorsComponent.reset()

                behaviorsComponent.isBehaviorActive("behavior1") shouldBe false
                behaviorsComponent.isBehaviorActive("behavior2") shouldBe false
            }
        }
    })
