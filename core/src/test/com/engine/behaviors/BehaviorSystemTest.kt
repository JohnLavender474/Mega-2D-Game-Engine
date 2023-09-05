package com.engine.behaviors

import com.engine.GameEntity
import com.engine.common.objects.Properties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class BehaviorSystemTest :
    DescribeSpec({
      describe("BehaviorSystem") {
        lateinit var behaviorComponent: BehaviorComponent
        lateinit var behaviorSystem: BehaviorSystem
        lateinit var behavior: Behavior
        lateinit var entity: GameEntity

        beforeEach {
          clearAllMocks()

          behavior = mockk {
            every { update(any()) } just Runs
            every { isActive() } returns true
          }

          behaviorComponent = spyk(BehaviorComponent())
          behaviorComponent.addBehavior("key", behavior)

          behaviorSystem = BehaviorSystem()

          entity =
              spyk(
                  object : GameEntity() {
                    override fun spawn(spawnProps: Properties) {}

                    override fun runOnDeath() {}

                    override fun reset() {}
                  })
        }

        it("should not add entity") { behaviorSystem.add(entity) shouldBe false }

        it("should add entity") {
          entity.addComponent(behaviorComponent)
          behaviorSystem.add(entity) shouldBe true
        }

        it("should process behaviors when turned on") {
          // if
          entity.addComponent(behaviorComponent)
          behaviorSystem.add(entity) shouldBe true

          // when
          behaviorSystem.update(1f)

          // then
          verify(exactly = 1) {
            behavior.update(1f)
            behaviorComponent.setActive("key", true)
          }
        }

        it("should not process behaviors when turned off") {
          // if
          entity.addComponent(behaviorComponent)
          behaviorSystem.add(entity) shouldBe true
          behaviorSystem.on = false

          // when
          behaviorSystem.update(1f)

          // then
          verify(exactly = 0) {
            behavior.update(any())
            behaviorComponent.setActive(any(), any())
          }
        }
      }
    })
