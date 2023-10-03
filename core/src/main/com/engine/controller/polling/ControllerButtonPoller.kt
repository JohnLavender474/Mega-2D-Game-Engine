package com.engine.controller.polling

import com.badlogic.gdx.Gdx
import com.engine.controller.ControllerUtils

/**
 * The actuator for a controller button. Contains the keyboard code and the controller code for the
 * button. The keyboard code is used to check if the button is pressed on the keyboard. The
 * controller code is used to check if the button is pressed on the controller.
 *
 * @param keyboardCode The keyboard code for the button.
 * @param controllerCode The controller code for the button.
 */
class ControllerButtonPoller(var keyboardCode: Int, var controllerCode: Int) {

  /**
   * Sets the keyboard code for the button.
   *
   * @return This instance for chaining.
   */
  fun isKeyboardButtonPressed() = Gdx.input.isKeyPressed(keyboardCode)

  /**
   * Returns if the button is pressed on the controller with the specified index.
   *
   * @param controllerIndex The index of the controller.
   */
  fun isControllerButtonPressed(controllerIndex: Int) =
      ControllerUtils.isControllerConnected(controllerIndex) &&
          ControllerUtils.getController(controllerIndex)?.isButtonPressed(controllerCode) == true
}
