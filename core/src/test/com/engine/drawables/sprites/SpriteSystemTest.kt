package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.engine.GameEntity
import com.engine.common.objects.Properties
import com.engine.drawables.sprites.ISprite
import com.engine.drawables.sprites.SpriteComponent
import com.engine.drawables.sprites.SpriteSystem
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class SpriteSystemTest :
    DescribeSpec({
      describe("SpriteSystem") {
        lateinit var mockBatch: Batch
        lateinit var mockSprite1: ISprite
        lateinit var mockSprite2: ISprite
        lateinit var mockSprite3: ISprite
        lateinit var mockSpriteComponent: SpriteComponent
        lateinit var entity: GameEntity
        lateinit var spriteSystem: SpriteSystem

        fun createMockSprite(priority: Int): ISprite = mockk {
          every { draw(mockBatch) } just Runs
          every { compareTo(any()) } returns priority
        }

        beforeEach {
          clearAllMocks()

          mockBatch = mockk()

          mockSprite1 = createMockSprite(1)
          mockSprite2 = createMockSprite(-1)
          mockSprite3 = createMockSprite(0)

          mockSpriteComponent = mockk {
            every { sprites } returns arrayListOf(mockSprite1, mockSprite2, mockSprite3)
          }

          entity =
              object : GameEntity(mockSpriteComponent) {
                override fun spawn(spawnProps: Properties) {}

                override fun destroy() {}

                override fun reset() {}
              }

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
