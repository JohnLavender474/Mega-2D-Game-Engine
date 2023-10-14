package com.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties
import com.engine.drawables.sprites.ISprite
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class AnimationsSystemTest :
    DescribeSpec({
      describe("AnimationSystem") {
        lateinit var entity: GameEntity
        lateinit var mockSprite: ISprite
        lateinit var mockAnimator: IAnimator
        lateinit var animationsComponent: AnimationsComponent
        lateinit var animationsSystem: AnimationsSystem

        beforeEach {
          clearAllMocks()
          entity =
              object : GameEntity(mockk()) {

                override fun spawn(spawnProps: Properties) {}

                override fun onDestroy() {}
              }
          entity.dead = false

          mockSprite = mockk { every { setRegion(any<TextureRegion>()) } just Runs }

          mockAnimator = mockk {
            every { animate(any(), any()) } answers
                {
                  val sprite = arg<ISprite>(0)
                  sprite.setRegion(TextureRegion())
                }
          }

          val map = ObjectMap<ISprite, IAnimator>()
          map.put(mockSprite, mockAnimator)
          animationsComponent = spyk(AnimationsComponent(entity, map))
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
