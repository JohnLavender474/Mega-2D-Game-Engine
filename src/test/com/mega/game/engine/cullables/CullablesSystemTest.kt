package com.mega.game.engine.cullables

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.GameEngine
import com.mega.game.engine.MockGameEntity
import com.mega.game.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CullablesSystemTest : DescribeSpec({

    lateinit var engine: GameEngine
    lateinit var cullablesSystem: CullablesSystem

    beforeEach {
        engine = GameEngine()
        cullablesSystem = CullablesSystem(engine)
        engine.systems.add(cullablesSystem)
    }

    describe("CullablesSystem") {
        it("should cull entities with cullables marked for culling") {
            val entities = Array<GameEntity>()
            for (i in 0..10) {
                val shouldCull = i % 2 == 0
                val entity = MockGameEntity()
                val cullablesComponent = CullablesComponent()
                cullablesComponent.cullables.put("key", object : ICullable {
                    override fun shouldBeCulled(delta: Float) = shouldCull
                })
                entity.addComponent(cullablesComponent)
                engine.spawn(entity)
                entities.add(entity)
            }

            engine.update(1f)
            engine.update(1f)

            for (i in 0..10) {
                val shouldCull = i % 2 == 0
                val entity = entities[i]
                val cullable = entity.getComponent(CullablesComponent::class)?.cullables?.get("key")
                cullable shouldNotBe null
                if (cullable != null) entity.state.spawned shouldBe !shouldCull
            }
        }

        it("should not cull entities with no cullable component") {
            val entities = Array<GameEntity>()

            for (i in 0..10) {
                val entity = MockGameEntity()
                engine.spawn(entity)
                entities.add(entity)
            }

            engine.update(1f)

            entities.forEach { entity ->
                entity.state.spawned shouldBe true
                entity.getComponent(CullablesComponent::class) shouldBe null
            }
        }
    }
})
