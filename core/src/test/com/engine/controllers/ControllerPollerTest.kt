package com.engine.controllers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class ControllerPollerTest :
    DescribeSpec({
      describe("ControllerPoller") {
        it("should initialize with the provided buttons") {
          val buttons =
              listOf(
                  "ButtonA" to ControllerButtonPoller({ 1 }, { 2 }),
                  "ButtonB" to ControllerButtonPoller({ 3 }, { 4 }))
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED
          controllerPoller.getButtonStatus("ButtonB") shouldBe ControllerButtonStatus.RELEASED
        }

        it("should poll both keyboard and controller by default") {
          val buttons = listOf("ButtonA" to ControllerButtonPoller({ 1 }, { 2 }))
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.pollingOption shouldBe ControllerPollingOption.BOTH
        }

        it("should update button status when run") {
          var pressed = true

          val buttonPoller =
              spyk(ControllerButtonPoller({ 1 }, { 2 })) {
                every { isKeyboardButtonPressed() } answers { pressed }
                every { isControllerButtonPressed() } answers { pressed }
              }
          val buttons = listOf("ButtonA" to buttonPoller)
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_PRESSED

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.PRESSED

          pressed = false

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_RELEASED

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED

          pressed = true

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_PRESSED
        }
      }
    })
