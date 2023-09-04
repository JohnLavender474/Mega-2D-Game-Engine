package com.engine.controllers

/** Interface for controller buttons. */
interface ControllerButtonActuator {

  /** Called when the button is now pressed and wasn't pressed before. */
  fun onJustPressed()

  /**
   * Called when the button is now pressed and was pressed before.
   *
   * @param delta The time in seconds since the last frame.
   */
  fun onPressContinued(delta: Float)

  /** Called when the button is now released and was pressed before. */
  fun onJustReleased()

  /**
   * Called when the button is now released and wasn't pressed before.
   *
   * @param delta The time in seconds since the last frame.
   */
  fun onReleaseContinued(delta: Float)
}
