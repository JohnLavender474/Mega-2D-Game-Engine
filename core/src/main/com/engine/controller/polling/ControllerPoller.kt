package com.engine.controller.polling

import com.badlogic.gdx.utils.ObjectMap
import com.engine.controller.buttons.Buttons
import com.engine.controller.buttons.ButtonStatus

/**
 * Polls the controller buttons and updates the status of each button.
 *
 * @param buttonMap The map of buttons to poll.
 */
class ControllerPoller(val buttonMap: Buttons) : IControllerPoller {

  internal val statusMap = ObjectMap<String, ButtonStatus>()
  override var on = true

  init {
    buttonMap.keys().forEach { statusMap.put(it, ButtonStatus.RELEASED) }
  }

  override fun getButtonStatus(name: String): ButtonStatus? = statusMap[name]

  override fun run() {
    if (!on) return

    buttonMap.forEach { e ->
      val key = e.key
      val button = e.value

      if (!statusMap.containsKey(key)) {
        statusMap.put(key, ButtonStatus.RELEASED)
      }
      val status = statusMap.get(key)

      val newStatus =
          if (button.enabled) {
            when (status) {
              ButtonStatus.RELEASED,
              ButtonStatus.JUST_RELEASED ->
                  if (button.pressed) ButtonStatus.JUST_PRESSED
                  else ButtonStatus.RELEASED
              else ->
                  if (button.pressed) ButtonStatus.PRESSED
                  else ButtonStatus.JUST_RELEASED
            }
          } else {
            if (status == ButtonStatus.JUST_RELEASED) {
              ButtonStatus.RELEASED
            } else {
              ButtonStatus.JUST_RELEASED
            }
          }

      statusMap.put(key, newStatus)
    }
  }
}
