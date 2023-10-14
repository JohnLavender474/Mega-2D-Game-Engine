package com.engine.drawables.sprites

import com.badlogic.gdx.utils.OrderedMap
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.mockk.*
import java.util.*

class SpriteSystemTest :
    DescribeSpec({
      describe("SpriteSystem") {
        lateinit var mockSprite1: GameSprite
        lateinit var mockSprite2: GameSprite
        lateinit var mockSprite3: GameSprite
        lateinit var mockSpriteComponent: SpriteComponent
        lateinit var entity: GameEntity
        lateinit var spritesQueue: TreeSet<ISprite>
        lateinit var spriteSystem: SpriteSystem

        beforeEach {
          clearAllMocks()

          mockSprite1 = spyk(GameSprite())
          mockSprite2 = spyk(GameSprite())
          mockSprite3 = spyk(GameSprite())

          val map = OrderedMap<String, ISprite>()
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

          spritesQueue = TreeSet()
          spriteSystem = SpriteSystem(spritesQueue)
          spriteSystem.on = true
          spriteSystem.add(entity)
        }

        it("should collect the sprites") {
          spriteSystem.update(1f)
          spritesQueue shouldContain mockSprite1
          spritesQueue shouldContain mockSprite2
          spritesQueue shouldContain mockSprite3
        }

        it("should not collect the sprites") {
          spriteSystem.on = false
          spriteSystem.update(1f)
          spritesQueue shouldNotContain mockSprite1
          spritesQueue shouldNotContain mockSprite2
          spritesQueue shouldNotContain mockSprite3
        }
      }
    })
