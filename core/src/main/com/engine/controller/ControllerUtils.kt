package com.engine.controller

import org.lwjgl.input.Controller
import org.lwjgl.input.Controllers

/** Utility class for controllers. */
object ControllerUtils {

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
