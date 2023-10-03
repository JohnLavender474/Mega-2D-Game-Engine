package com.engine.controller.polling

import com.badlogic.gdx.utils.ObjectMap
import com.engine.controller.ControllerButtonMap

/**
 * The polling option for the controller poller.
 *
 * KEYBOARD: Only poll the keyboard. CONTROLLER: Only poll the controller. BOTH: Poll both the
 * keyboard and the controller.
 */
enum class ControllerPollingOption {
  KEYBOARD,
  CONTROLLER,
  BOTH
}

/**
 * Polls the controller, i.e. checks the status of each controller button. The collection of buttons
 * passed into the constructor is internally converted into a map. The key is the name of the button
 * and the value is the button itself. The controller should be polled every frame.
 *
 * @param buttonMap The map of buttons to poll.
 */
class ControllerPoller(
    val buttonMap: ControllerButtonMap,
    var pollingOption: ControllerPollingOption = ControllerPollingOption.BOTH
) : IControllerPoller {

  internal val statusMap = ObjectMap<String, ControllerButtonStatus>()
  override var on = true

  init {
    buttonMap.getKeys().forEach { statusMap.put(it, ControllerButtonStatus.RELEASED) }
  }

  /**
   * Gets the status of the button mapped to the key.
   *
   * @param name The button name.
   * @return The status of the button mapped to the key.
   */
  override fun getButtonStatus(name: String): ControllerButtonStatus? = statusMap[name]

  /**
   * Runs the controller poller. This should be called every frame. This will update the status of
   * each button. If a button is pressed, the status will be updated to JUST_PRESSED or PRESSED. If
   * a button is released, the status will be updated to JUST_RELEASED or RELEASED. Otherwise, the
   * status will remain the same.
   */
  override fun run() {
    if (!on) return

    buttonMap.getKeys().forEach {
      val poller =
          buttonMap.getButtonPoller(it)
              ?: throw IllegalStateException("Button key $it does not have a poller")

      if (!statusMap.containsKey(it)) {
        statusMap.put(it, ControllerButtonStatus.RELEASED)
      }
      val status = statusMap.get(it)

      val pressed =
          when (pollingOption) {
            ControllerPollingOption.KEYBOARD -> poller.isKeyboardButtonPressed()
            ControllerPollingOption.CONTROLLER -> poller.isControllerButtonPressed(0)
            ControllerPollingOption.BOTH ->
                poller.isKeyboardButtonPressed() || poller.isControllerButtonPressed(0)
          }

      val newStatus =
          if (buttonMap.isEnabled(it)) {
            when (status) {
              ControllerButtonStatus.RELEASED,
              ControllerButtonStatus.JUST_RELEASED ->
                  if (pressed) ControllerButtonStatus.JUST_PRESSED
                  else ControllerButtonStatus.RELEASED
              else ->
                  if (pressed) ControllerButtonStatus.PRESSED
                  else ControllerButtonStatus.JUST_RELEASED
            }
          } else {
            if (status == ControllerButtonStatus.JUST_RELEASED) {
              ControllerButtonStatus.RELEASED
            } else {
              ControllerButtonStatus.JUST_RELEASED
            }
          }

      statusMap.put(it, newStatus)
    }
  }
}
