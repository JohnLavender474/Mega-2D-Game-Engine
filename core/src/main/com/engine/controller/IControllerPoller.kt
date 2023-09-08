package com.engine.controller

import com.engine.common.interfaces.Activatable

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
  fun getButtonStatus(name: String): ControllerButtonStatus?
}
