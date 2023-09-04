package com.engine.behaviors

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class BehaviorComponentTest :
    DescribeSpec({
      describe("BehaviorComponent") {
        val behavior1 =
            object : Behavior() {
              override fun evaluate(delta: Float): Boolean {
                return delta > 0.0f
              }

              override fun init() {}

              override fun act(delta: Float) {}

              override fun end() {}
            }

        val behavior2 =
            object : Behavior() {
              override fun evaluate(delta: Float): Boolean {
                return delta < 0.0f
              }

              override fun init() {}

              override fun act(delta: Float) {}

              override fun end() {}
            }

        val behaviorComponent = BehaviorComponent()

        it("should add and check behaviors") {
          behaviorComponent.addBehavior("behavior1", behavior1)
          behaviorComponent.addBehavior("behavior2", behavior2)

          behaviorComponent.isBehaviorActive("behavior1") shouldBe false
          behaviorComponent.isBehaviorActive("behavior2") shouldBe false

          behaviorComponent.setActive("behavior1", true)
          behaviorComponent.isBehaviorActive("behavior1") shouldBe true
        }

        it("should check if any behaviors are active") {
          behaviorComponent.reset()

          behaviorComponent.addBehavior("behavior1", behavior1)
          behaviorComponent.addBehavior("behavior2", behavior2)

          behaviorComponent.setActive("behavior1", true)
          behaviorComponent.setActive("behavior2", false)

          behaviorComponent.isAnyBehaviorActive(listOf("behavior1", "behavior2")) shouldBe true
          behaviorComponent.isAnyBehaviorActive(listOf("behavior2")) shouldBe false
        }

        it("should check if all behaviors are active") {
          behaviorComponent.reset()

          behaviorComponent.addBehavior("behavior1", behavior1)
          behaviorComponent.addBehavior("behavior2", behavior2)

          behaviorComponent.setActive("behavior1", true)
          behaviorComponent.setActive("behavior2", false)

          behaviorComponent.areAllBehaviorsActive(listOf("behavior1", "behavior2")) shouldBe false
          behaviorComponent.areAllBehaviorsActive(listOf("behavior1")) shouldBe true
        }

        it("should reset behaviors and active behaviors") {
          behaviorComponent.addBehavior("behavior1", behavior1)
          behaviorComponent.addBehavior("behavior2", behavior2)

          behaviorComponent.setActive("behavior1", true)
          behaviorComponent.setActive("behavior2", true)

          behaviorComponent.reset()

          behaviorComponent.isBehaviorActive("behavior1") shouldBe false
          behaviorComponent.isBehaviorActive("behavior2") shouldBe false
        }
      }
    })
