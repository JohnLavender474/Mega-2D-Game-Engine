package com.engine.controller.polling

import com.badlogic.gdx.utils.ObjectMap
import com.engine.controller.ControllerUtils
import com.engine.controller.buttons.ButtonStatus
import com.engine.controller.buttons.Buttons

/**
 * Polls the controller buttons and updates the status of each button.
 *
 * @param buttons The map of buttons to poll.
 */
class ControllerPoller(private val buttons: Buttons) : IControllerPoller {

  internal val statusMap = ObjectMap<String, ButtonStatus>()
  override var on = true

  init {
    buttons.keys().forEach { statusMap.put(it, ButtonStatus.RELEASED) }
  }

  override fun getButtonStatus(name: String): ButtonStatus? = statusMap[name]

  override fun run() {
    if (!on) return

    buttons.forEach { e ->
      val key = e.key
      val button = e.value

      if (!button.enabled) return@forEach

      if (!statusMap.containsKey(key)) statusMap.put(key, ButtonStatus.RELEASED)
      val status = statusMap.get(key)

      var pressed = button.keyboardCode.let { ControllerUtils.isKeyboardKeyPressed(it) }
      if (!pressed)
          button.controllerCode?.let { pressed = ControllerUtils.isControllerKeyPressed(it) }

      val newStatus =
          when (status) {
            ButtonStatus.RELEASED,
            ButtonStatus.JUST_RELEASED ->
                if (pressed) ButtonStatus.JUST_PRESSED else ButtonStatus.RELEASED
            else -> if (pressed) ButtonStatus.PRESSED else ButtonStatus.JUST_RELEASED
          }

      statusMap.put(key, newStatus)
    }
  }
}
