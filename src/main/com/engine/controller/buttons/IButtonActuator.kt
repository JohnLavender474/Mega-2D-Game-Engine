package com.engine.controller.buttons

import com.engine.controller.polling.IControllerPoller

/** Interface for controller buttons. */
interface IButtonActuator {

    /** Called when the button is now pressed and wasn't pressed before. */
    fun onJustPressed(poller: IControllerPoller)

    /**
     * Called when the button is now pressed and was pressed before.
     *
     * @param delta The time in seconds since the last frame.
     */
    fun onPressContinued(poller: IControllerPoller, delta: Float)

    /** Called when the button is now released and was pressed before. */
    fun onJustReleased(poller: IControllerPoller)

    /**
     * Called when the button is now released and wasn't pressed before.
     *
     * @param delta The time in seconds since the last frame.
     */
    fun onReleaseContinued(poller: IControllerPoller, delta: Float)
}
