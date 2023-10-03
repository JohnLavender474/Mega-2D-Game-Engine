package com.engine.controller

import com.engine.controller.polling.ControllerButtonPoller
import com.engine.controller.polling.ControllerButtonStatus
import com.engine.controller.polling.ControllerPoller
import com.engine.controller.polling.ControllerPollingOption
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class ControllerPollerTest :
    DescribeSpec({
      describe("ControllerPoller") {
        it("should initialize with the provided buttons") {
          val buttons = ControllerButtonMap()
          buttons.putButtonPoller("ButtonA", ControllerButtonPoller(1, 2))
          buttons.putButtonPoller("ButtonB", ControllerButtonPoller(3, 4))
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED
          controllerPoller.getButtonStatus("ButtonB") shouldBe ControllerButtonStatus.RELEASED
        }

        it("should poll both keyboard and controller by default") {
          val buttons = ControllerButtonMap()
          buttons.putButtonPoller("ButtonA", ControllerButtonPoller(1, 2))
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.pollingOption shouldBe ControllerPollingOption.BOTH
        }

        it("should update button status when run") {
          var pressed = true

          val buttonPoller =
              spyk(ControllerButtonPoller(1, 2)) {
                every { isKeyboardButtonPressed() } answers { pressed }
                every { isControllerButtonPressed(any()) } answers { pressed }
              }
          val buttons = ControllerButtonMap()
          buttons.putButtonPoller("ButtonA", buttonPoller)
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

        it("should put status for new button") {
          val pressed = true

          val buttonPollerA =
              spyk(ControllerButtonPoller(1, 2)) {
                every { isKeyboardButtonPressed() } answers { pressed }
                every { isControllerButtonPressed(any()) } answers { pressed }
              }
          val buttons = ControllerButtonMap()
          buttons.putButtonPoller("ButtonA", buttonPollerA)
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_PRESSED
          controllerPoller.getButtonStatus("ButtonB") shouldBe null

          val buttonPollerB =
              spyk(ControllerButtonPoller(1, 2)) {
                every { isKeyboardButtonPressed() } answers { pressed }
                every { isControllerButtonPressed(any()) } answers { pressed }
              }
          buttons.putButtonPoller("ButtonB", buttonPollerB)

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.PRESSED
          controllerPoller.getButtonStatus("ButtonB") shouldBe ControllerButtonStatus.JUST_PRESSED
        }

        it("should release button when disabled") {
          val pressed = true

          val buttonPollerA =
              spyk(ControllerButtonPoller(1, 2)) {
                every { isKeyboardButtonPressed() } answers { pressed }
                every { isControllerButtonPressed(any()) } answers { pressed }
              }
          val buttons = ControllerButtonMap()
          buttons.putButtonPoller("ButtonA", buttonPollerA)
          val controllerPoller = ControllerPoller(buttons)

          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_PRESSED
          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.PRESSED

          buttons.setEnabled("ButtonA", false)

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_RELEASED
          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.RELEASED

          buttons.setEnabled("ButtonA", true)

          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.JUST_PRESSED
          controllerPoller.run()
          controllerPoller.getButtonStatus("ButtonA") shouldBe ControllerButtonStatus.PRESSED
        }
      }
    })
