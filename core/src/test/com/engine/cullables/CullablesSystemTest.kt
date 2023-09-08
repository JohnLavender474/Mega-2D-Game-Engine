package com.engine.cullables

import com.engine.GameEntity
import com.engine.SimpleMockEntity
import com.engine.common.extensions.toImmutableCollection
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.numericFloats
import io.kotest.property.checkAll

class CullablesSystemTest :
    DescribeSpec({
      describe("CullablesSystem") {
        it("should cull entities with cullables marked for culling") {
          val cullablesSystem = CullablesSystem()
          val entities = mutableListOf<GameEntity>()

          // Create a few entities with cullable components
          for (i in 0..10) {
            val shouldCull = i % 2 == 0

            val entity = SimpleMockEntity()
            val cullablesComponent = CullablesComponent()

            cullablesComponent.cullables.add(
              object : Cullable {
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

        it("should not cull entities with no cullable components") {
          val cullablesSystem = CullablesSystem()
          val entities = mutableListOf<GameEntity>()

          // Create a few entities with cullable components
          for (i in 0..10) {
            val entity = SimpleMockEntity()
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
