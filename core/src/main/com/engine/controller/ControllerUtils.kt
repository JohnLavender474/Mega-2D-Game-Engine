package com.engine.controller

import com.badlogic.gdx.Gdx
import org.lwjgl.input.Controller
import org.lwjgl.input.Controllers

/** Utility class for controllers. */
object ControllerUtils {

  fun isKeyboardKeyPressed(key: Int) = Gdx.input.isKeyPressed(key)

  fun isControllerKeyPressed(key: Int) = isControllerKeyPressed(0, key)

  fun isControllerKeyPressed(index: Int, key: Int) =
      getController(index)?.isButtonPressed(key) ?: false

  /**
   * Gets the controller with the specified index.
   *
   * @param index The index of the controller.
   * @return The controller with the specified index.
   */
  fun getController(index: Int): Controller? = Controllers.getController(index)

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
  fun getController() =
      if (Controllers.getControllerCount() > 0) Controllers.getController(0) else null

  /**
   * Checks if the first controller is connected.
   *
   * @return True if the first controller is connected.
   */
  fun isControllerConnected() = getController() != null
}
