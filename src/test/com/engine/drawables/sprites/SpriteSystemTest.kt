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
            lateinit var mockSpritesComponent: SpritesComponent
            lateinit var entity: GameEntity
            lateinit var spritesQueue: TreeSet<GameSprite>
            lateinit var spritesSystem: SpritesSystem

            beforeEach {
                clearAllMocks()

                mockSprite1 = spyk(GameSprite())
                mockSprite2 = spyk(GameSprite())
                mockSprite3 = spyk(GameSprite())

                val map = OrderedMap<String, GameSprite>()
                map.put("1", mockSprite1)
                map.put("2", mockSprite2)
                map.put("3", mockSprite3)
                mockSpritesComponent = mockk {
                    every { sprites } returns map
                    every { update(any()) } just Runs
                }

                entity = GameEntity(mockk())
                entity.addComponent(mockSpritesComponent)
                entity.dead = false

                spritesQueue = TreeSet()
                spritesSystem = SpritesSystem { spritesQueue.add(it) }
                spritesSystem.on = true
                spritesSystem.add(entity)
            }

            it("should collect the sprites") {
                spritesSystem.update(1f)
                spritesQueue shouldContain mockSprite1
                spritesQueue shouldContain mockSprite2
                spritesQueue shouldContain mockSprite3
            }

            it("should not collect the sprites") {
                spritesSystem.on = false
                spritesSystem.update(1f)
                spritesQueue shouldNotContain mockSprite1
                spritesQueue shouldNotContain mockSprite2
                spritesQueue shouldNotContain mockSprite3
            }
        }
    })
