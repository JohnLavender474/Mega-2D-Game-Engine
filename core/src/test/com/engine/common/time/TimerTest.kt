package com.engine.common.time

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TimerTest :
    DescribeSpec({
      describe("Timer") {
        val timer = Timer(5f)
        val mockRunnable =
            object : TimeMarkedRunnable(1f) {
              override fun run() {}
            }

        beforeEach {
          timer.clearRunnables()
          timer.reset()
        }

        it("should update correctly") {
          timer.update(1f)
          timer.time shouldBe 1f
        }

        it("should reset correctly") {
          timer.setRunnables(listOf(mockRunnable))

          timer.runnableQueue.size shouldBe 1
          timer.update(1f)
          timer.time shouldBe 1f
          timer.runnableQueue.size shouldBe 0

          timer.reset()
          timer.time shouldBe 0f
          timer.runnableQueue.size shouldBe 1
        }

        it("should calculate ratio correctly") {
          timer.update(1f)
          timer.getRatio() shouldBe 0.2f
        }

        it("should check if it's at the beginning correctly") {
          timer.isAtBeginning() shouldBe true
          timer.update(1f)
          timer.isAtBeginning() shouldBe false
        }

        it("should check if it's finished correctly") {
          timer.isFinished() shouldBe false
          timer.update(5f)
          timer.isFinished() shouldBe true
        }

        it("should check if it's just finished correctly") {
          timer.update(1f)
          timer.isFinished() shouldBe false
          timer.isJustFinished() shouldBe false

          timer.update(4f)
          timer.time shouldBe 5f
          timer.isFinished() shouldBe true
          timer.isJustFinished() shouldBe true

          timer.update(1f)
          timer.time shouldBe 5f
          timer.isFinished() shouldBe true
          timer.isJustFinished() shouldBe false

          timer.reset()
          timer.time shouldBe 0f

          timer.update(4f)
          timer.time shouldBe 4f
          timer.isFinished() shouldBe false
          timer.isJustFinished() shouldBe false

          timer.update(1f)
          timer.time shouldBe 5f
          timer.isFinished() shouldBe true
          timer.isJustFinished() shouldBe true

          timer.update(1f)
          timer.time shouldBe 5f
          timer.isFinished() shouldBe true
          timer.isJustFinished() shouldBe false
        }

        it("should set runnables correctly") {
          timer.runnableQueue.size shouldBe 0
          timer.setRunnables(listOf(mockRunnable))
          timer.runnableQueue.size shouldBe 1
        }

      }
    })
