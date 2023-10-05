package com.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent
import com.engine.controller.buttons.IButtonActuator

/**
 * A component that contains a map of [IButtonActuator]s. The key is the name of the button and the
 * value is the button itself.
 *
 * @param actuators The map of actuators.
 * @see IButtonActuator
 */
class ControllerComponent(val actuators: ObjectMap<String, IButtonActuator>) : IGameComponent {

  /**
   * Creates a [ControllerComponent] with the given actuators.
   *
   * @param _actuators The actuators to add to the component.
   */
  constructor(
      vararg _actuators: Pair<String, IButtonActuator>
  ) : this(
      ObjectMap<String, IButtonActuator>().apply { _actuators.forEach { put(it.first, it.second) } })
}
