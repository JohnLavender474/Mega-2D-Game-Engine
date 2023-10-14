package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.engine.drawables.IDrawable
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class SpriteSystemTest :
    DescribeSpec({
      describe("SpriteSystem") {
        lateinit var mockSprite1: ISprite
        lateinit var mockSprite2: ISprite
        lateinit var mockSprite3: ISprite
        lateinit var mockSpriteComponent: SpriteComponent
        lateinit var entity: GameEntity
        lateinit var spritesQueue: Array<IDrawable<Batch>>
        lateinit var spriteSystem: SpriteSystem

        beforeEach {
          clearAllMocks()

          mockSprite1 = mockk()
          mockSprite2 = mockk()
          mockSprite3 = mockk()

          val map = OrderedMap<String, IDrawable<Batch>>()
          map.put("1", mockSprite1)
          map.put("2", mockSprite2)
          map.put("3", mockSprite3)
          mockSpriteComponent = mockk {
            every { sprites } returns map
            every { update(any()) } just Runs
          }

          entity = GameEntity(mockk())
          entity.addComponent(mockSpriteComponent)
          entity.dead = false

          spritesQueue = spyk(Array())
          spriteSystem = SpriteSystem(spritesQueue)
          spriteSystem.on = true
          spriteSystem.add(entity)
        }

        it("should collect the sprites") {
          spriteSystem.update(1f)
          verify(exactly = 1) { spritesQueue.add(mockSprite1) }
          verify(exactly = 1) { spritesQueue.add(mockSprite2) }
          verify(exactly = 1) { spritesQueue.add(mockSprite3) }
        }

        it("should not collect the sprites") {
          spriteSystem.on = false
          spriteSystem.update(1f)
          verify(exactly = 0) { spritesQueue.add(mockSprite1) }
          verify(exactly = 0) { spritesQueue.add(mockSprite2) }
          verify(exactly = 0) { spritesQueue.add(mockSprite3) }
        }
      }
    })
