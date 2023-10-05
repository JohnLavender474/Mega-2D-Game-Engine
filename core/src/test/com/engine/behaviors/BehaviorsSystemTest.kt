package com.engine.behaviors

import com.engine.entities.GameEntity
import com.engine.common.objects.Properties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class BehaviorsSystemTest :
    DescribeSpec({
      describe("BehaviorSystem") {
        lateinit var behaviorsComponent: BehaviorsComponent
        lateinit var behaviorsSystem: BehaviorsSystem
        lateinit var behavior: Behavior
        lateinit var entity: GameEntity

        beforeEach {
          clearAllMocks()

          behavior = mockk {
            every { update(any()) } just Runs
            every { isActive() } returns true
          }

          behaviorsComponent = spyk(BehaviorsComponent())
          behaviorsComponent.addBehavior("key", behavior)

          behaviorsSystem = BehaviorsSystem()

          entity =
              spyk(
                  object : GameEntity() {
                    override fun spawn(spawnProps: Properties) {}

                    override fun onDestroy() {}
                  })
          entity.dead = false
        }

        it("should not add entity") { behaviorsSystem.add(entity) shouldBe false }

        it("should add entity") {
          entity.addComponent(behaviorsComponent)
          behaviorsSystem.add(entity) shouldBe true
        }

        it("should process behaviors when turned on") {
          // if
          entity.addComponent(behaviorsComponent)
          behaviorsSystem.add(entity) shouldBe true

          // when
          behaviorsSystem.update(1f)

          // then
          verify(exactly = 1) {
            behavior.update(1f)
            behaviorsComponent.setActive("key", true)
          }
        }

        it("should not process behaviors when turned off") {
          // if
          entity.addComponent(behaviorsComponent)
          behaviorsSystem.add(entity) shouldBe true
          behaviorsSystem.on = false

          // when
          behaviorsSystem.update(1f)

          // then
          verify(exactly = 0) {
            behavior.update(any())
            behaviorsComponent.setActive(any(), any())
          }
        }
      }
    })
