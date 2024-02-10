package com.engine.spawns

import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle
import com.engine.events.Event
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk

class SpawnerTest :
    DescribeSpec({
        lateinit var spawn: Spawn
        lateinit var spawnSupplier: () -> Spawn
        lateinit var shouldBeCulledSupplier: (Float) -> Boolean

        beforeEach {
            clearAllMocks()
            spawn = mockk()
            spawnSupplier = { spawn }
            shouldBeCulledSupplier = { false }
        }

        describe("SpawnerForEvent") {
            it("should spawn when predicate matches event") {
                val event1 = Event("eventKey1", Properties())
                val event2 = Event("eventKey2", Properties())

                val spawner = SpawnerForEvent({ it == event2 }, spawnSupplier)

                spawner.onEvent(event1)
                spawner.test(0f) shouldBe false
                spawner.get() shouldBe null

                spawner.onEvent(event2)
                spawner.test(0f) shouldBe true
                spawner.get() shouldBe spawn
            }
        }

        describe("SpawnerForBoundsEntered") {
            it("should spawn when bounds overlap") {
                val thisBounds = GameRectangle(0f, 0f, 2f, 2f)
                val otherBounds = GameRectangle(3f, 3f, 2f, 2f)

                val spawner =
                    SpawnerForBoundsEntered(
                        spawnSupplier, { thisBounds }, { otherBounds }, shouldBeCulledSupplier
                    )

                spawner.test(0f) shouldBe false
                spawner.get() shouldBe null

                otherBounds.setPosition(1f, 1f)
                spawner.test(0f) shouldBe true
                spawner.get() shouldBe spawn
            }
        }
    })
