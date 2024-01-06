package com.engine.controller.polling

import com.engine.common.interfaces.IActivatable
import com.engine.controller.buttons.ButtonStatus

/**
 * Interface for controller pollers. A controller poller polls the controller for button status and
 * returns the status of the buttons. This interface extends [IActivatable] and [Runnable] so that it
 * can be activated and run. The [run] method should update the status of each button.
 */
interface IControllerPoller : IActivatable, Runnable {

  /**
   * Gets the status of the button mapped to the key.
   *
   * @param key The button key.
   * @return The status of the button mapped to the key.
   */
  fun getStatus(key: Any): ButtonStatus?

  /**
   * Returns if the button mapped to the given [key] is pressed.
   *
   * @param key The key of the button to check.
   * @return If the button mapped to the given [key] is pressed.
   */
  fun isPressed(key: Any): Boolean {
    val status = getStatus(key)
    return status == ButtonStatus.PRESSED || status == ButtonStatus.JUST_PRESSED
  }

  /**
   * Returns if the button mapped to the given [key] is just pressed.
   *
   * @param key The key of the button to check.
   * @return If the button mapped to the given [key] is just pressed.
   */
  fun isJustPressed(key: Any): Boolean {
    val status = getStatus(key)
    return status == ButtonStatus.JUST_PRESSED
  }

  /**
   * Returns if the button mapped to the given [key] is just released.
   *
   * @param key The key of the button to check.
   * @return If the button mapped to the given [key] is just released.
   */
  fun isJustReleased(key: Any): Boolean {
    val status = getStatus(key)
    return status == ButtonStatus.JUST_RELEASED
  }

  /**
   * Returns if the button mapped to the given [key] is released.
   *
   * @param key The key of the button to check.
   * @return If the button mapped to the given [key] is released.
   */
  fun isReleased(key: Any): Boolean {
    val status = getStatus(key)
    return status == ButtonStatus.RELEASED || status == ButtonStatus.JUST_RELEASED
  }
}
