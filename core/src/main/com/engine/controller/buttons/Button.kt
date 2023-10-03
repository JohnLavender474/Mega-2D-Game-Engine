package com.engine.controller.buttons

/** A button with a poller and an enabled flag */
class Button(var poller: (() -> Boolean)? = null, var enabled: Boolean = true) {

  val pressed: Boolean
    get() = poller?.invoke() ?: false
}
