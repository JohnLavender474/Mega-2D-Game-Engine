package com.engine.controller.buttons

/** A button with a poller for the keyboard and controller and an enabled flag */
class Button(
    var keyboardCode: (() -> Int)?,
    var controllerCode: (() -> Int)?,
    var enabled: Boolean = true
)
