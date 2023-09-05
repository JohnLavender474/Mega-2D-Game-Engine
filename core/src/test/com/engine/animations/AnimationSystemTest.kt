package com.engine.animations

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.GameEntity
import com.engine.common.objects.Properties
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class AnimationSystemTest :
    DescribeSpec({
      describe("AnimationSystem") {
        lateinit var entity: GameEntity
        lateinit var mockSprite: Sprite
        lateinit var mockAnimator: IAnimator
        lateinit var animationComponent: AnimationComponent
        lateinit var animationSystem: AnimationSystem

        beforeEach {
          clearAllMocks()
          entity =
              object : GameEntity() {

                override fun spawn(spawnProps: Properties) {}

                override fun runOnDeath() {}

                override fun reset() {}
              }

          mockSprite = mockk { every { setRegion(any<TextureRegion>()) } just Runs }

          mockAnimator = mockk {
            every { animate(any(), any()) } answers
                {
                  val sprite = arg<Sprite>(0)
                  sprite.setRegion(TextureRegion())
                }
          }

          animationComponent = spyk(AnimationComponent(hashMapOf(mockSprite to mockAnimator)))
          animationSystem = spyk(AnimationSystem())

          entity.addComponent(animationComponent)
          animationSystem.add(entity)
        }

        it("process") {
          animationSystem.update(1f)
          verify(exactly = 1) { mockAnimator.animate(mockSprite, 1f) }
          verify(exactly = 1) { mockSprite.setRegion(any<TextureRegion>()) }
        }
      }
    })
