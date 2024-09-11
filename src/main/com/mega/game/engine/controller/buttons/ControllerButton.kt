package com.mega.game.engine.controller.buttons

/**
 * An abstraction for the keyboard and gamepad controller of a button. Contains a property for the keyboard code and
 * controller code to be assigned to the button, along with a flag for whether this button is enabled.
 *
 * @property keyboardCode the code to map this controller button abstraction to a keyboard key
 * @property controllerCode the code to map this controller button abstraction to a gamepad key
 * @property enabled whether this controller button is enabled
 */
class ControllerButton(var keyboardCode: Int, var controllerCode: Int? = null, var enabled: Boolean = true)
