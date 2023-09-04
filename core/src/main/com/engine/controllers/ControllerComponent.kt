package com.engine.controllers

import com.engine.GameComponent

/**
 * A component that contains a map of [ControllerButtonActuator]s. The key is the name of the button
 * and the value is the button itself.
 *
 * @param actuators The map of actuators.
 * @see ControllerButtonActuator
 */
class ControllerComponent(val actuators: Map<String, ControllerButtonActuator>) : GameComponent
