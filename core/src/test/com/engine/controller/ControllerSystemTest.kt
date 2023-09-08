package com.engine.controller

import com.engine.SimpleMockEntity
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class ControllerSystemTest :
    DescribeSpec({
      describe("ControllerSystem") {
        it("should call appropriate methods on actuators when processing") {
          val mockControllerPoller = mockk<IControllerPoller>()
          val controllerSystem = ControllerSystem(mockControllerPoller)

          val entity = SimpleMockEntity()

          val actuator =
              mockk<ControllerButtonActuator> {
                every { onJustPressed() } just Runs
                every { onPressContinued(any()) } just Runs
                every { onJustReleased() } just Runs
                every { onReleaseContinued(any()) } just Runs
              }

          val controllerComponent = ControllerComponent(mapOf("ButtonA" to actuator))
          entity.addComponent(controllerComponent)

          controllerSystem.add(entity)

          for (buttonStatus in 0..4) {
            when (buttonStatus) {
              0 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ControllerButtonStatus.JUST_PRESSED
              1 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ControllerButtonStatus.PRESSED
              2 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ControllerButtonStatus.JUST_RELEASED
              3 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ControllerButtonStatus.RELEASED
            }

            controllerSystem.update(0.1f)

            when (buttonStatus) {
              0 -> verify(exactly = 1) { actuator.onJustPressed() }
              1 -> verify { actuator.onPressContinued(0.1f) }
              2 -> verify { actuator.onJustReleased() }
              3 -> verify { actuator.onReleaseContinued(0.1f) }
            }
          }
        }
      }
    })
