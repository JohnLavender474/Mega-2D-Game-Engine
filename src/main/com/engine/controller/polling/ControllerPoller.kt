package com.engine.controller.polling

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.computeValues
import com.engine.controller.ControllerUtils
import com.engine.controller.buttons.ButtonStatus
import com.engine.controller.buttons.Buttons

/**
 * Polls the controller buttons and updates the status of each button.
 *
 * @param buttons The map of buttons to poll.
 */
open class ControllerPoller(val buttons: Buttons) : IControllerPoller {

    /**
     * Whether the poller is on. If the poller is turned off, then keyboard/controller input will not be processed.
     * In this case, the status of all buttons that are [ButtonStatus.PRESSED] or [ButtonStatus.JUST_PRESSED] will be
     * set to [ButtonStatus.JUST_RELEASED], and the status of all buttons that are [ButtonStatus.RELEASED] or
     * [ButtonStatus.JUST_RELEASED] will be set to [ButtonStatus.RELEASED].
     */
    override var on = true

    private val statusMap = ObjectMap<Any, ButtonStatus>()

    init {
        buttons.keys().forEach { statusMap.put(it, ButtonStatus.RELEASED) }
    }

    override fun getStatus(key: Any): ButtonStatus? = statusMap[key]

    override fun run() {
        buttons.forEach { e ->
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
