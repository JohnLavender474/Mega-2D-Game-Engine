package com.engine

import com.engine.common.objects.ImmutableCollection
import com.engine.common.objects.Properties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.verify

class GameEngineTest :
    DescribeSpec({
      describe("GameEngine") {
        lateinit var props1: Properties
        lateinit var props2: Properties
        lateinit var mockEntity1: GameEntity
        lateinit var mockEntity2: GameEntity

        class MockComponent1 : GameComponent
        class MockComponent2 : GameComponent
        lateinit var mockComponent1: MockComponent1
        lateinit var mockComponent2: MockComponent2

        lateinit var mockSystem1: GameSystem
        lateinit var mockSystem2: GameSystem
        lateinit var systems: List<GameSystem>

        lateinit var gameEngine: GameEngine

        beforeEach {
          clearAllMocks()

          props1 = Properties()
          props2 = Properties()

          mockEntity1 =
              spyk(
                  object : GameEntity() {
                    override fun spawn(spawnProps: Properties) {}

                    override fun runOnDeath() {}

                    override fun reset() {}
                  })
          mockEntity2 =
              spyk(
                  object : GameEntity() {
                    override fun spawn(spawnProps: Properties) {}

                    override fun runOnDeath() {}

                    override fun reset() {}
                  })

          mockComponent1 = MockComponent1()
          mockComponent2 = MockComponent2()

          mockEntity1.putComponent(mockComponent1)
          mockEntity2.putComponent(mockComponent2)

          mockSystem1 =
              object : GameSystem(MockComponent1::class) {
                override fun process(
                    on: Boolean,
                    entities: ImmutableCollection<GameEntity>,
                    delta: Float
                ) {}
              }
          mockSystem2 =
              object : GameSystem(MockComponent2::class) {
                override fun process(
                    on: Boolean,
                    entities: ImmutableCollection<GameEntity>,
                    delta: Float
                ) {}
              }

          systems = listOf(mockSystem1, mockSystem2)

          gameEngine = GameEngine(systems, true)
        }

        it("should spawn and update entities") {
          gameEngine.spawn(mockEntity1, props1)
          gameEngine.spawn(mockEntity2, props2)

          gameEngine.entitiesToAdd.contains(mockEntity1 to props1) shouldBe true
          gameEngine.entitiesToAdd.contains(mockEntity2 to props2) shouldBe true

          mockSystem1.contains(mockEntity1) shouldBe false
          mockSystem2.contains(mockEntity2) shouldBe false

          gameEngine.update(1f)

          gameEngine.entitiesToAdd.contains(mockEntity1 to props1) shouldBe false
          gameEngine.entitiesToAdd.contains(mockEntity2 to props2) shouldBe false

          mockEntity1.dead shouldBe false
          mockEntity2.dead shouldBe false

          gameEngine.entities.contains(mockEntity1) shouldBe true
          gameEngine.entities.contains(mockEntity2) shouldBe true

          mockSystem1.contains(mockEntity1) shouldBe true
          mockSystem2.contains(mockEntity2) shouldBe true

          verify(exactly = 1) { mockEntity1.spawn(props1) }
          verify(exactly = 1) { mockEntity2.spawn(props1) }
        }

        it("should purge entities when reset is called") {
          gameEngine.spawn(mockEntity1, props1)
          gameEngine.spawn(mockEntity2, props2)

          gameEngine.update(1f)

          gameEngine.entities.contains(mockEntity1) shouldBe true
          gameEngine.entities.contains(mockEntity2) shouldBe true
          mockEntity1.dead shouldBe false
          mockEntity2.dead shouldBe false

          gameEngine.purge()

          gameEngine.entities.isEmpty() shouldBe true
          mockEntity1.dead shouldBe true
          mockEntity1.dead shouldBe true
          mockSystem1.contains(mockEntity1) shouldBe false
          mockSystem2.contains(mockEntity2) shouldBe false

          verify(exactly = 1) { mockEntity1.runOnDeath() }
          verify(exactly = 1) { mockEntity2.runOnDeath() }
        }

        it("should add entities to systems correctly") {
          gameEngine.spawn(mockEntity1, props1)
          gameEngine.spawn(mockEntity2, props2)

          gameEngine.update(1f)

          mockSystem1.contains(mockEntity1) shouldBe true
          mockSystem2.contains(mockEntity2) shouldBe true
        }
      }
    })
