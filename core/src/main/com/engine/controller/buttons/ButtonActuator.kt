package com.engine.controller.buttons

import com.engine.controller.polling.IControllerPoller

/** Implementation for [IButtonActuator] that uses lambdas. */
class ButtonActuator(
    private val onJustPressed: ((IControllerPoller) -> Unit)? = null,
    private val onPressContinued: ((IControllerPoller, Float) -> Unit)? = null,
    private val onJustReleased: ((IControllerPoller) -> Unit)? = null,
    private val onReleaseContinued: ((IControllerPoller, Float) -> Unit)? = null
) : IButtonActuator {

  override fun onJustPressed(poller: IControllerPoller) {
    onJustPressed?.invoke(poller)
  }

  override fun onPressContinued(poller: IControllerPoller, delta: Float) {
    onPressContinued?.invoke(poller, delta)
  }

  override fun onJustReleased(poller: IControllerPoller) {
    onJustReleased?.invoke(poller)
  }

  override fun onReleaseContinued(poller: IControllerPoller, delta: Float) {
    onReleaseContinued?.invoke(poller, delta)
  }
}
