package com.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent
import com.engine.controller.polling.ControllerButtonActuator

/**
 * A component that contains a map of [ControllerButtonActuator]s. The key is the name of the button
 * and the value is the button itself.
 *
 * @param actuators The map of actuators.
 * @see ControllerButtonActuator
 */
class ControllerComponent(val actuators: ObjectMap<String, ControllerButtonActuator>) : IGameComponent
