package com.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent
import com.engine.controller.buttons.IButtonActuator
import com.engine.entities.IGameEntity

/**
 * A component that contains a map of [IButtonActuator]s. The key is the name of the button and the
 * value is the button itself.
 *
 * @param actuators The map of actuators.
 * @see IButtonActuator
 */
class ControllerComponent(
    override val entity: IGameEntity,
    val actuators: ObjectMap<Any, () -> IButtonActuator?>
) : IGameComponent {

  /**
   * Creates a [ControllerComponent] with the given actuators.
   *
   * @param _actuators The actuators to add to the component.
   */
  constructor(
      entity: IGameEntity,
      vararg _actuators: Pair<Any, () -> IButtonActuator?>
  ) : this(
      entity,
      ObjectMap<Any, () -> IButtonActuator?>().apply {
        _actuators.forEach { put(it.first, it.second) }
      })
}
