package com.mega.game.engine.controller.buttons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.controller.ControllerUtils

/**
 * A basic implemention of [IControllerButton].
 *
 * @property keyboardCode the code to map this controller button abstraction to a keyboard key
 * @property controllerCode the code to map this controller button abstraction to a physical controller key;
 *                          default value is null
 * @property alternateActuators a map of lambdas where if any value returns true then [isPressed] will return true
 * @property enabled whether this controller button is enabled
 */
class ControllerButton(
    var keyboardCode: Int,
    var controllerCode: Int? = null,
    var alternateActuators: OrderedMap<Any, () -> Boolean> = OrderedMap(),
    var enabled: Boolean = true
) : IControllerButton {

    fun isKeyboardKeyPressed() = Gdx.input.isKeyPressed(keyboardCode)

    fun isControllerKeyPressed() = ControllerUtils.isControllerConnected() &&
            controllerCode?.let { ControllerUtils.isControllerKeyPressed(it) } == true

    fun isAnyAlternateActuatorPressed() = alternateActuators.values().any { it.invoke() }

    override fun isPressed() = isKeyboardKeyPressed() || isControllerKeyPressed() || isAnyAlternateActuatorPressed()

    override fun isEnabled() = enabled
}
