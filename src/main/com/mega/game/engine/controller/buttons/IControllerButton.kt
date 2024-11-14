package com.mega.game.engine.controller.buttons

/**
 * Interface for a controller button. A controller button can be pressed/unpressed and enabled/disabled.
 */
interface IControllerButton {

    /**
     * Returns true if the button is pressed, otherwise false.
     *
     * @return true if the button is pressed, otherwise false
     */
    fun isPressed(): Boolean

    /**
     * Returns true if the button is enabled, otherwise false.
     *
     * @return true if the button is enabled, otherwise false
     */
    fun isEnabled(): Boolean
}