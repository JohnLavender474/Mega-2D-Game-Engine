package com.engine.controller

/**
 * Interface for controller button pollers. A controller button poller polls the controller for
 * button status and returns the status of the buttons.
 */
interface IControllerButtonPoller {

  /**
   * Gets the status of the button mapped to the keyboard key.
   *
   * @return The status of the button mapped to the key.
   */
  fun isKeyboardButtonPressed(): Boolean

  /**
   * Gets the status of the button mapped to the controller button.
   *
   * @param controllerIndex The index of the controller.
   * @return The status of the button mapped to the controller button.
   */
  fun isControllerButtonPressed(controllerIndex: Int): Boolean
}
