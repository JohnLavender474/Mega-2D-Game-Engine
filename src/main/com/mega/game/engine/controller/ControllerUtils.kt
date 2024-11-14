package com.mega.game.engine.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.Controllers

/** Utility object for controllers. */
object ControllerUtils {

    /**
     * Checks if the specified key is pressed on the keyboard.
     *
     * @param key The key to check.
     * @return True if the specified key is pressed on the keyboard.
     */
    fun isKeyboardKeyPressed(key: Int) = Gdx.input.isKeyPressed(key)

    /**
     * Checks if the specified key is pressed on the first controller.
     *
     * @param key The key to check.
     * @return True if the specified key is pressed on the first controller.
     */
    fun isControllerKeyPressed(key: Int) = isControllerKeyPressed(0, key)

    /**
     * Checks if the specified key is pressed on the specified controller.
     *
     * @param index The index of the controller.
     * @param key The key to check.
     * @return True if the specified key is pressed on the specified controller.
     */
    fun isControllerKeyPressed(index: Int, key: Int) = getController(index)?.getButton(key) == true

    /**
     * Gets the controller from the specified index, or null if one does not exist. Returns null rather than throwing
     * an exception if the index is out of bounds.
     *
     * @param index The index of the controller.
     * @return The controller at the specified index, or null.
     */
    fun getController(index: Int): Controller? =
        try {
            Controllers.getControllers().get(index)
        } catch (_: Exception) {
            null
        }

    /**
     * Checks if the controller with the specified index is connected.
     *
     * @param index The index of the controller.
     * @return True if the controller with the specified index is connected.
     */
    fun isControllerConnected(index: Int) = getController(index) != null

    /**
     * Gets the first controller.
     *
     * @return The first controller.
     */
    fun getController() = if (!Controllers.getControllers().isEmpty) Controllers.getControllers().get(0) else null

    /**
     * Checks if the first controller is connected.
     *
     * @return True if the first controller is connected.
     */
    fun isControllerConnected() = getController() != null
}
