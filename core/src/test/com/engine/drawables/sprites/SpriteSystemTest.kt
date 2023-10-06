package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.objects.Properties
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class SpriteSystemTest :
    DescribeSpec({
      describe("SpriteSystem") {
        lateinit var mockBatch: Batch
        lateinit var mockSprite1: IGameSprite
        lateinit var mockSprite2: IGameSprite
        lateinit var mockSprite3: IGameSprite
        lateinit var mockSpriteComponent: SpriteComponent
        lateinit var entity: GameEntity
        lateinit var spriteSystem: SpriteSystem

        fun createMockSprite(priority: Int): IGameSprite = mockk {
          every { draw(mockBatch) } just Runs
          every { compareTo(any()) } returns priority
        }

        beforeEach {
          clearAllMocks()

          mockBatch = mockk()

          mockSprite1 = createMockSprite(1)
          mockSprite2 = createMockSprite(-1)
          mockSprite3 = createMockSprite(0)

          val map = OrderedMap<String, IGameSprite>()
          map.put("1", mockSprite1)
          map.put("2", mockSprite2)
          map.put("3", mockSprite3)
          mockSpriteComponent = mockk { every { sprites } returns map }

          entity =
              object : GameEntity() {
                override fun spawn(spawnProps: Properties) {}

                override fun onDestroy() {}
              }
          entity.addComponent(mockSpriteComponent)
          entity.dead = false

          spriteSystem = SpriteSystem(mockBatch)
          spriteSystem.on = true
          spriteSystem.add(entity)
        }

        it("should draw the sprite") {
          spriteSystem.update(1f)
          verify(exactly = 1) { mockSprite1.draw(mockBatch) }
          verify(exactly = 1) { mockSprite2.draw(mockBatch) }
          verify(exactly = 1) { mockSprite3.draw(mockBatch) }
        }

        it("should not draw the sprite") {
          spriteSystem.on = false
          spriteSystem.update(1f)
          verify(exactly = 0) { mockSprite1.draw(mockBatch) }
          verify(exactly = 0) { mockSprite2.draw(mockBatch) }
          verify(exactly = 0) { mockSprite3.draw(mockBatch) }
        }

        it("should draw in sorted order") {
          spriteSystem.update(1f)
          verifyOrder {
            mockSprite2.draw(mockBatch)
            mockSprite3.draw(mockBatch)
            mockSprite1.draw(mockBatch)
          }
        }
      }
    })
