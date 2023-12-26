package com.engine.controller.polling

import com.engine.common.interfaces.Activatable
import com.engine.controller.buttons.ButtonStatus

/**
 * Interface for controller pollers. A controller poller polls the controller for button status and
 * returns the status of the buttons. This interface extends [Activatable] and [Runnable] so that it
 * can be activated and run. The [run] method should update the status of each button.
 */
interface IControllerPoller : Activatable, Runnable {

  /**
   * Gets the status of the button mapped to the key.
   *
   * @param name The button name.
   * @return The status of the button mapped to the key.
   */
  fun getButtonStatus(name: String): ButtonStatus?

  /**
   * Returns if the button mapped to the given [name] is pressed.
   *
   * @param name The name of the button to check.
   * @return If the button mapped to the given [name] is pressed.
   */
  fun isButtonPressed(name: String): Boolean {
    val status = getButtonStatus(name)
    return status == ButtonStatus.PRESSED || status == ButtonStatus.JUST_PRESSED
  }

  /**
   * Returns if the button mapped to the given [name] is just pressed.
   *
   * @param name The name of the button to check.
   * @return If the button mapped to the given [name] is just pressed.
   */
  fun isButtonJustPressed(name: String): Boolean {
    val status = getButtonStatus(name)
    return status == ButtonStatus.JUST_PRESSED
  }

  /**
   * Returns if the button mapped to the given [name] is just released.
   *
   * @param name The name of the button to check.
   * @return If the button mapped to the given [name] is just released.
   */
  fun isButtonJustReleased(name: String): Boolean {
    val status = getButtonStatus(name)
    return status == ButtonStatus.JUST_RELEASED
  }

  /**
   * Returns if the button mapped to the given [name] is released.
   *
   * @param name The name of the button to check.
   * @return If the button mapped to the given [name] is released.
   */
  fun isButtonReleased(name: String): Boolean {
    val status = getButtonStatus(name)
    return status == ButtonStatus.RELEASED || status == ButtonStatus.JUST_RELEASED
  }
}
