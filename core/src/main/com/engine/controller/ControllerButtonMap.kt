package com.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.engine.controller.polling.ControllerButtonPoller

/**
 * A map of controller buttons and their respective [ControllerButtonPoller] instances. Also
 * contains a map of the enabled status of each button.
 */
class ControllerButtonMap {

  private val buttonMap = ObjectMap<String, ControllerButtonPoller>()
  private val enabledMap = ObjectMap<String, Boolean>()

  /**
   * Gets the keys of the button map.
   *
   * @return The keys of the button map.
   */
  internal fun getKeys() = buttonMap.keys()

  /**
   * Puts a button into the button map.
   *
   * @param name The name of the button.
   * @param button The button.
   */
  fun putButtonPoller(name: String, button: ControllerButtonPoller) {
    if (!buttonMap.containsKey(name)) {
      enabledMap.put(name, true)
    }
    buttonMap.put(name, button)
  }

  /**
   * Sets the keyboard code for the button. Does nothing if the button has not been first put into
   * this map via [putButtonPoller]
   *
   * @param name The name of the button.
   * @param keyboardCode The keyboard code.
   * @return True if the button was successfully updated, false otherwise.
   */
  fun setButtonKeyboardCode(name: String, keyboardCode: Int): Boolean {
    if (!buttonMap.containsKey(name)) return false
    buttonMap[name]?.keyboardCode = keyboardCode
    return true
  }

  /**
   * Sets the controller code for the button. Does nothing if the button has not been first put into
   * this map via [putButtonPoller]
   *
   * @param name The name of the button.
   * @param controllerCode The controller code.
   * @return True if the button was successfully updated, false otherwise.
   */
  fun setButtonControllerCode(name: String, controllerCode: Int): Boolean {
    if (!buttonMap.containsKey(name)) return false
    buttonMap[name]?.controllerCode = controllerCode
    return true
  }

  /**
   * Gets the button poller mapped to the name.
   *
   * @param name The name of the button.
   * @return The button poller mapped to the name.
   */
  fun getButtonPoller(name: String): ControllerButtonPoller? = buttonMap[name]

  /**
   * Sets the enabled status of the button. Does nothing if the button has not been first put into
   * this map via [putButtonPoller]
   *
   * @param name The name of the button.
   * @param enabled The enabled status.
   * @return True if the button was successfully updated, false otherwise.
   */
  fun setEnabled(name: String, enabled: Boolean): Boolean {
    if (!buttonMap.containsKey(name)) return false
    enabledMap.put(name, enabled)
    return true
  }

  /**
   * Gets the enabled status of the button.
   *
   * @param name The name of the button.
   * @return The enabled status of the button.
   */
  fun isEnabled(name: String) = enabledMap[name] ?: false
}
