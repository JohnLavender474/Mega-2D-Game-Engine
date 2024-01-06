package com.engine.controller

import com.engine.common.objects.ImmutableCollection
import com.engine.controller.buttons.ButtonStatus
import com.engine.controller.polling.IControllerPoller
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * System for controllers. This system will call the appropriate methods on the actuators of the
 * controller buttons. This system requires a [IControllerPoller] to poll the controller buttons.
 */
class ControllerSystem(private val poller: IControllerPoller) :
    GameSystem(ControllerComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    entities.forEach {
      val component = it.getComponent(ControllerComponent::class)!!
      val actuators = component.actuators

      for (entry in actuators) {
        val key = entry.key
        val actuator = entry.value() ?: continue

        val status = poller.getStatus(key) ?: continue
        when (status) {
          ButtonStatus.JUST_PRESSED -> actuator.onJustPressed(poller)
          ButtonStatus.PRESSED -> actuator.onPressContinued(poller, delta)
          ButtonStatus.JUST_RELEASED -> actuator.onJustReleased(poller)
          ButtonStatus.RELEASED -> actuator.onReleaseContinued(poller, delta)
        }
      }
    }
  }
}
