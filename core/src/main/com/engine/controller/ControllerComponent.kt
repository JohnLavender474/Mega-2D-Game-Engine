package com.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent
import com.engine.controller.buttons.ButtonActuator

/**
 * A component that contains a map of [ButtonActuator]s. The key is the name of the button
 * and the value is the button itself.
 *
 * @param actuators The map of actuators.
 * @see ButtonActuator
 */
class ControllerComponent(val actuators: ObjectMap<String, ButtonActuator>) : IGameComponent
