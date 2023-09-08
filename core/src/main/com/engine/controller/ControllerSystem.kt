package com.engine.controller

import com.engine.GameEntity
import com.engine.GameSystem
import com.engine.common.objects.ImmutableCollection

/**
 * System for controllers. This system will call the appropriate methods on the actuators of the
 * controller buttons. This system requires a [ControllerPoller] to poll the controller buttons.
 */
class ControllerSystem(private val poller: IControllerPoller) :
    GameSystem(ControllerComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) return

    entities.forEach {
      val component = it.getComponent(ControllerComponent::class)

      component?.actuators?.forEach { (name, actuator) ->
        when (poller.getButtonStatus(name)) {
          ControllerButtonStatus.JUST_PRESSED -> actuator.onJustPressed()
          ControllerButtonStatus.PRESSED -> actuator.onPressContinued(delta)
          ControllerButtonStatus.JUST_RELEASED -> actuator.onJustReleased()
          ControllerButtonStatus.RELEASED -> actuator.onReleaseContinued(delta)
          else -> {}
        }
      }
    }
  }
}
