package com.engine.controller

import com.badlogic.gdx.Gdx

/**
 * The actuator for a controller button. Contains the keyboard code and the controller code for the
 * button. The keyboard code is used to check if the button is pressed on the keyboard. The
 * controller code is used to check if the button is pressed on the controller.
 *
 * @param keyboardCodeSupplier The keyboard code supplier for the button.
 * @param controllerCodeSupplier The controller code supplier for the button.
 */
class ControllerButtonPoller(
    var keyboardCodeSupplier: () -> Int,
    var controllerCodeSupplier: () -> Int
) : IControllerButtonPoller {

  /**
   * Sets the keyboard code for the button.
   *
   * @return This instance for chaining.
   */
  override fun isKeyboardButtonPressed() = Gdx.input.isKeyPressed(keyboardCodeSupplier())

  /**
   * Returns if the button is pressed on the controller with the specified index.
   *
   * @param controllerIndex The index of the controller.
   */
  override fun isControllerButtonPressed(controllerIndex: Int) =
      ControllerUtils.isControllerConnected(controllerIndex) &&
          ControllerUtils.getController(controllerIndex)?.isButtonPressed(controllerCodeSupplier()) == true
}
