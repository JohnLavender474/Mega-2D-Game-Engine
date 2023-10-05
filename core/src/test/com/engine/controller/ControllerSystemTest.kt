package com.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.engine.controller.buttons.ButtonActuator
import com.engine.controller.buttons.ButtonStatus
import com.engine.controller.polling.IControllerPoller
import com.engine.entities.GameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class ControllerSystemTest :
    DescribeSpec({
      describe("ControllerSystem") {
        it("should call appropriate methods on actuators when processing") {
          val mockControllerPoller = mockk<IControllerPoller>()
          val controllerSystem = ControllerSystem(mockControllerPoller)

          val entity = GameEntity()

          val actuator =
              mockk<ButtonActuator> {
                every { onJustPressed(any()) } just Runs
                every { onPressContinued(any(), any()) } just Runs
                every { onJustReleased(any()) } just Runs
                every { onReleaseContinued(any(), any()) } just Runs
              }

          val map = ObjectMap<String, ButtonActuator>()
          map.put("ButtonA", actuator)
          val controllerComponent = ControllerComponent(map)
          entity.addComponent(controllerComponent)

          controllerSystem.add(entity)

          for (buttonStatus in 0..4) {
            when (buttonStatus) {
              0 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ButtonStatus.JUST_PRESSED
              1 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ButtonStatus.PRESSED
              2 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ButtonStatus.JUST_RELEASED
              3 ->
                  every { mockControllerPoller.getButtonStatus("ButtonA") } returns
                      ButtonStatus.RELEASED
            }

            controllerSystem.update(0.1f)

            when (buttonStatus) {
              0 -> verify(exactly = 1) { actuator.onJustPressed(mockControllerPoller) }
              1 -> verify { actuator.onPressContinued(mockControllerPoller, 0.1f) }
              2 -> verify { actuator.onJustReleased(mockControllerPoller) }
              3 -> verify { actuator.onReleaseContinued(mockControllerPoller, 0.1f) }
            }
          }
        }
      }
    })
