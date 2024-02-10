package com.engine.spawns

import com.badlogic.gdx.utils.Array
import com.engine.common.extensions.gdxArrayOf
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify

class SpawnsManagerTest :
    DescribeSpec({
        describe("SpawnsManager") {
            class MockSpawner : ISpawner {
                var test = false
                var cull = false
                var spawned = false
                override var respawnable = true

                override fun get() = mockk<Spawn>()

                override fun test(delta: Float) = test && !spawned

                override fun shouldBeCulled(delta: Float) = cull
            }

            lateinit var spawners: Array<ISpawner>
            lateinit var spawner1: MockSpawner
            lateinit var spawner2: MockSpawner
            lateinit var spawner3: MockSpawner
            lateinit var spawnsManager: SpawnsManager

            beforeEach {
                clearAllMocks()
                spawner1 = spyk(MockSpawner())
                spawner2 = spyk(MockSpawner())
                spawner3 = spyk(MockSpawner())
                spawners = gdxArrayOf(spawner1, spawner2, spawner3)
                spawnsManager = spyk(SpawnsManager())
                spawnsManager.setSpawners(spawners)
            }

            it("should spawn correctly") {
                spawner1.cull = false
                spawner2.cull = false
                spawner3.cull = false

                spawner1.test = true
                spawner2.test = true
                spawner3.test = false

                val deltaTime = 0.1f
                spawnsManager.update(deltaTime)

                verify(exactly = 1) { spawner1.test(deltaTime) }
                verify(exactly = 1) { spawner2.test(deltaTime) }
                verify(exactly = 1) { spawner3.test(deltaTime) }

                verify(exactly = 1) { spawner1.get() }
                verify(exactly = 1) { spawner2.get() }
                verify(exactly = 0) { spawner3.get() }

                val spawned = spawnsManager.getSpawnsAndClear()
                spawned.size shouldBe 2
            }

            it("should cull spawners correctly") {
                spawner1.cull = false
                spawner2.cull = true
                spawner3.cull = true

                spawner1.test = true
                spawner2.test = true
                spawner3.test = true

                val deltaTime = 0.1f
                spawnsManager.update(deltaTime)

                verify(exactly = 1) { spawner1.test(deltaTime) }
                verify(exactly = 0) { spawner2.test(deltaTime) }
                verify(exactly = 0) { spawner3.test(deltaTime) }

                verify(exactly = 1) { spawner1.get() }
                verify(exactly = 0) { spawner2.get() }
                verify(exactly = 0) { spawner3.get() }

                val spawned = spawnsManager.getSpawnsAndClear()
                spawned.size shouldBe 1
            }

            it("should not re-add spawns") {
                spawner1.cull = false
                spawner2.cull = false
                spawner3.cull = false

                spawner1.test = true
                spawner2.test = true
                spawner3.test = true

                val deltaTime = 0.1f
                spawnsManager.update(deltaTime)

                verify(exactly = 1) { spawner1.test(deltaTime) }
                verify(exactly = 1) { spawner2.test(deltaTime) }
                verify(exactly = 1) { spawner3.test(deltaTime) }

                verify(exactly = 1) { spawner1.get() }
                verify(exactly = 1) { spawner2.get() }
                verify(exactly = 1) { spawner3.get() }

                spawnsManager.spawns.size shouldBe 3
                spawnsManager.spawners.size shouldBe 3

                spawner1.spawned = true
                spawner2.spawned = true
                spawner3.spawned = true

                spawnsManager.update(deltaTime)

                verify(exactly = 2) { spawner1.test(deltaTime) }
                verify(exactly = 2) { spawner2.test(deltaTime) }
                verify(exactly = 2) { spawner3.test(deltaTime) }

                verify(exactly = 1) { spawner1.get() }
                verify(exactly = 1) { spawner2.get() }
                verify(exactly = 1) { spawner3.get() }

                spawnsManager.spawns.size shouldBe 3
                spawnsManager.spawners.size shouldBe 3
            }

            it("should re-add spawns") {
                spawner1.cull = false
                spawner2.cull = false
                spawner3.cull = false

                spawner1.test = true
                spawner2.test = true
                spawner3.test = true

                val deltaTime = 0.1f
                spawnsManager.update(deltaTime)

                verify(exactly = 1) { spawner1.test(deltaTime) }
                verify(exactly = 1) { spawner2.test(deltaTime) }
                verify(exactly = 1) { spawner3.test(deltaTime) }

                verify(exactly = 1) { spawner1.get() }
                verify(exactly = 1) { spawner2.get() }
                verify(exactly = 1) { spawner3.get() }

                spawnsManager.spawns.size shouldBe 3
                spawnsManager.spawners.size shouldBe 3

                spawnsManager.update(deltaTime)

                verify(exactly = 2) { spawner1.test(deltaTime) }
                verify(exactly = 2) { spawner2.test(deltaTime) }
                verify(exactly = 2) { spawner3.test(deltaTime) }

                verify(exactly = 2) { spawner1.get() }
                verify(exactly = 2) { spawner2.get() }
                verify(exactly = 2) { spawner3.get() }

                spawnsManager.spawns.size shouldBe 6
                spawnsManager.spawners.size shouldBe 3
            }

            it("should reset correctly") {
                spawner1.cull = false
                spawner2.cull = false
                spawner3.cull = false

                spawner1.test = true
                spawner2.test = true
                spawner3.test = true

                val deltaTime = 0.1f
                spawnsManager.update(deltaTime)

                verify(exactly = 1) { spawner1.test(deltaTime) }
                verify(exactly = 1) { spawner2.test(deltaTime) }
                verify(exactly = 1) { spawner3.test(deltaTime) }

                verify(exactly = 1) { spawner1.get() }
                verify(exactly = 1) { spawner2.get() }
                verify(exactly = 1) { spawner3.get() }

                spawnsManager.reset()

                spawnsManager.spawns.size shouldBe 0
                spawnsManager.spawners.size shouldBe 0
            }
        }
    })
