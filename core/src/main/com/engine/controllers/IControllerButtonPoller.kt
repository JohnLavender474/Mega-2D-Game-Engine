package com.engine.controllers

interface IControllerButtonPoller {

  fun isKeyboardButtonPressed(): Boolean

  fun isControllerButtonPressed(controllerIndex: Int): Boolean
}
