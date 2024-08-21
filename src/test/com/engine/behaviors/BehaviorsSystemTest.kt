package com.engine.behaviors

import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.verify

class BehaviorsSystemTest : DescribeSpec({
    describe("BehaviorSystem") {
        lateinit var behaviorsComponent: BehaviorsComponent
        lateinit var behaviorsSystem: BehaviorsSystem
        lateinit var behavior: AbstractBehaviorImpl
        lateinit var entity: GameEntity

        beforeEach {
            clearAllMocks()

            entity = GameEntity()

            behavior = spyk(FunctionalBehaviorImpl({ true }, { }, { }, { }))

            behaviorsComponent = spyk(BehaviorsComponent())
            behaviorsComponent.addBehavior("key", behavior)

            behaviorsSystem = BehaviorsSystem()
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
            }
        }

        it("should reset the behavior") {
            entity.addComponent(behaviorsComponent)
            behaviorsSystem.add(entity) shouldBe true
            behaviorsSystem.on = true

            behaviorsSystem.update(1f)
            behaviorsComponent.getBehavior("key")!!.reset()

            verify { behavior.reset() }
            behavior.isActive() shouldBe false

            behavior.update(1f)

            verify { behavior.init() }
        }
    }
})
