package com.mega.game.engine.controller.polling

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.controller.ControllerUtils
import com.mega.game.engine.controller.buttons.ButtonStatus
import com.mega.game.engine.controller.buttons.ControllerButton
import com.mega.game.engine.controller.buttons.ControllerButtons

/**
 * Polls the controller buttons and updates the status of each button in the [run] method.
 *
 * @param controllerButtons the map of controller buttons to poll
 */
open class ControllerPoller(val controllerButtons: ControllerButtons) : IControllerPoller {

    /**
     * Whether the poller is on. If the poller is turned off, then keyboard/controller input will not be processed.
     * In this case, the status of all buttons that are [ButtonStatus.PRESSED] or [ButtonStatus.JUST_PRESSED] will be
     * set to [ButtonStatus.JUST_RELEASED], and the status of all buttons that are [ButtonStatus.RELEASED] or
     * [ButtonStatus.JUST_RELEASED] will be set to [ButtonStatus.RELEASED].
     */
    override var on = true

    private val statusMap = ObjectMap<Any, ButtonStatus>()
    private var initialized = false

    /**
     * Initializes the [statusMap] so that each key from [controllerButtons] is contained, and assigns to each key the
     * value of [ButtonStatus.RELEASED].
     */
    override fun init() = controllerButtons.keys().forEach { statusMap.put(it, ButtonStatus.RELEASED) }

    /**
     * Retrieves the current status of the button from [statusMap].
     *
     * @param key the button key
     * @return the current button status
     */
    override fun getStatus(key: Any): ButtonStatus? = statusMap[key]

    /**
     * Runs the poller to update the status of each button in the [statusMap] property using the buttons from the
     * [controllerButtons] property. If the poller has not been initialized yet, then [init] is called. If during the
     * run cycle a button in [controllerButtons] is not contained in [statusMap], then the button is added to the status
     * map with a value of [ButtonStatus.RELEASED].
     *
     * For each button, if [ControllerButton.enabled] is false, then the button is not polled, meaning that if the
     * previous status of the button was `pressed`, then the value for the button is set to [ButtonStatus.JUST_RELEASED],
     * or if the previous state of the button was `unpressed`, then the value will be [ButtonStatus.RELEASED].
     */
    override fun run() {
        if (!initialized) {
            init()
            initialized = true
        }

        controllerButtons.forEach { e ->
            val key = e.key
            val button = e.value

            if (!statusMap.containsKey(key)) statusMap.put(key, ButtonStatus.RELEASED)
            val status = statusMap.get(key)!!

            val newStatus: ButtonStatus = if (on) {
                if (button.enabled) {
                    var pressed = ControllerUtils.isKeyboardKeyPressed(button.keyboardCode)
                    if (!pressed && button.controllerCode != null) pressed =
                        ControllerUtils.isControllerKeyPressed(button.controllerCode!!)
                    when (status) {
                        ButtonStatus.RELEASED,
                        ButtonStatus.JUST_RELEASED ->
                            if (pressed) ButtonStatus.JUST_PRESSED else ButtonStatus.RELEASED

                        else -> if (pressed) ButtonStatus.PRESSED else ButtonStatus.JUST_RELEASED
                    }
                } else if (status == ButtonStatus.JUST_RELEASED) ButtonStatus.RELEASED
                else ButtonStatus.JUST_RELEASED
            } else when (status) {
                ButtonStatus.PRESSED, ButtonStatus.JUST_PRESSED -> ButtonStatus.JUST_RELEASED
                ButtonStatus.RELEASED, ButtonStatus.JUST_RELEASED -> ButtonStatus.RELEASED
            }

            statusMap.put(key, newStatus)
        }
    }
}
