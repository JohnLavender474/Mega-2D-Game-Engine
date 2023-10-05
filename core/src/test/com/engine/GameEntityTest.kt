package com.engine

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks

class GameEntityTest :
    DescribeSpec({
      describe("Entity class") {
        val entity =
            object : GameEntity() {

              override fun spawn(spawnProps: Properties) {}

              override fun destroy() {}
            }

        class MockComponent : IGameComponent

        val mockComponent = MockComponent()

        beforeEach {
          clearAllMocks()
          entity.clearComponents()
        }

        it("should have the correct initial properties") {
          entity.components shouldBe ObjectMap()
          entity.properties shouldBe Properties()
          entity.dead shouldBe true
        }

        it("should put, get, and remove properties correctly") {
          val key = "propertyKey"
          val value = "propertyValue"

          entity.putProperty(key, value)
          entity.getProperty(key) shouldBe value
          entity.hasProperty(key) shouldBe true

          entity.removeProperty(key)
          entity.getProperty(key) shouldBe null
          entity.hasProperty(key) shouldBe false
        }

        it("should put and get components correctly") {
          entity.addComponent(mockComponent)
          entity.getComponent(MockComponent::class) shouldBe mockComponent
          entity.hasComponent(MockComponent::class) shouldBe true

          entity.removeComponent(MockComponent::class)
          entity.getComponent(MockComponent::class) shouldBe null
          entity.hasComponent(MockComponent::class) shouldBe false
        }

        it("should clear components correctly") {
          entity.addComponent(mockComponent)
          entity.clearComponents()

          entity.getComponent(MockComponent::class) shouldBe null
          entity.hasComponent(MockComponent::class) shouldBe false
        }

        it("should generate a valid string representation") {
          val componentName = "MockComponent"
          entity.addComponent(mockComponent)

          entity.toString() shouldBe "${entity::class.simpleName}: $componentName"
        }
      }
    })
