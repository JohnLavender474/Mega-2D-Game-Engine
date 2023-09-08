package com.engine.controller

interface IControllerButtonPoller {

  fun isKeyboardButtonPressed(): Boolean

  fun isControllerButtonPressed(controllerIndex: Int): Boolean
}
