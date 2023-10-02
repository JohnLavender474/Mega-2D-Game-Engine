package com.engine.animations

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class AnimationsSystemTest :
    DescribeSpec({
      describe("AnimationSystem") {
        lateinit var entity: GameEntity
        lateinit var mockSprite: Sprite
        lateinit var mockAnimator: IAnimator
        lateinit var animationsComponent: AnimationsComponent
        lateinit var animationsSystem: AnimationsSystem

        beforeEach {
          clearAllMocks()
          entity =
              object : GameEntity() {

                override fun spawn(spawnProps: Properties) {}

                override fun destroy() {}

                override fun reset() {}
              }
          entity.dead = false

          mockSprite = mockk { every { setRegion(any<TextureRegion>()) } just Runs }

          mockAnimator = mockk {
            every { animate(any(), any()) } answers
                {
                  val sprite = arg<Sprite>(0)
                  sprite.setRegion(TextureRegion())
                }
          }

          val map = ObjectMap<Sprite, IAnimator>()
          map.put(mockSprite, mockAnimator)
          animationsComponent = spyk(AnimationsComponent(map))
          animationsSystem = spyk(AnimationsSystem())

          entity.addComponent(animationsComponent)
          animationsSystem.add(entity)
        }

        it("process") {
          animationsSystem.update(1f)
          verify(exactly = 1) { mockAnimator.animate(mockSprite, 1f) }
          verify(exactly = 1) { mockSprite.setRegion(any<TextureRegion>()) }
        }
      }
    })
