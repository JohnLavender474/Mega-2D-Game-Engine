package com.engine.cullables

import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk

class CullablesSystemTest :
    DescribeSpec({
      describe("CullablesSystem") {
        it("should cull entities with cullables marked for culling") {
          val cullablesSystem = CullablesSystem()
          val entities = mutableListOf<GameEntity>()

          // Create a few entities with cullable componentMap
          for (i in 0..10) {
            val shouldCull = i % 2 == 0

            val entity = GameEntity(mockk())
            val cullablesComponent = CullablesComponent(entity)

            cullablesComponent.cullables.add(
              object : ICullable {
                override fun shouldBeCulled() = shouldCull
              })
            entity.addComponent(cullablesComponent)

            entities.add(entity)
          }

          // Add entities to system
          cullablesSystem.on = true
          cullablesSystem.addAll(entities)

          // Update the system
          cullablesSystem.update(1f)

          // Verify that entities marked for culling are dead
          entities.forEach { entity ->
            val cullable = entity.getComponent(CullablesComponent::class)?.cullables?.get(0)
            cullable shouldNotBe null

            if (cullable != null) {
              val shouldCull = cullable.shouldBeCulled()
              entity.dead shouldBe shouldCull
            }
          }
        }

        it("should not cull entities with no cullable componentMap") {
          val cullablesSystem = CullablesSystem()
          val entities = mutableListOf<GameEntity>()

          // Create a few entities with cullable componentMap
          for (i in 0..10) {
            val entity = GameEntity(mockk())
            entities.add(entity)
          }

          // Add entities to system
          cullablesSystem.on = true
          cullablesSystem.addAll(entities)

          // Update the system
          cullablesSystem.update(1f)

          // Verify that no entities are marked as dead
          entities.forEach { entity ->
            entity.dead shouldBe false
            entity.getComponent(CullablesComponent::class) shouldBe null
          }
        }
      }
    })
