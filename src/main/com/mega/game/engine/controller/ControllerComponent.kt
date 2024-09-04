package com.mega.game.engine.controller

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.components.IGameComponent
import com.mega.game.engine.controller.buttons.IButtonActuator
import java.util.function.Supplier

/**
 * A component that contains a map of [IButtonActuator]s. The key is the name of the button and the
 * value is the button itself.
 *
 * @param actuators The map of actuators.
 * @see IButtonActuator
 */
class ControllerComponent(val actuators: ObjectMap<Any, () -> IButtonActuator?>) : IGameComponent {

    /**
     * Creates a [ControllerComponent] with the given actuators.
     *
     * @param _actuators The actuators to add to the component.
     */
    constructor(vararg _actuators: Pair<Any, () -> IButtonActuator?>) : this(ObjectMap<Any, () -> IButtonActuator?>().apply {
        _actuators.forEach { put(it.first, it.second) }
    })

    /**
     * Puts an actuator into the map with the given name and supplier.
     *
     * @param name The name of the actuator.
     * @param actuator The supplier of the actuator.
     */
    fun putActuator(name: Any, actuator: () -> IButtonActuator?) {
        actuators.put(name, actuator)
    }

    /**
     * Puts an actuator into the map with the given name and supplier.
     *
     * @param name The name of the actuator.
     * @param actuator The supplier of the actuator.
     */
    fun putActuator(name: Any, actuator: Supplier<IButtonActuator?>) {
        actuators.put(name, actuator::get)
    }

    /**
     * Puts an actuator into the map with the given name.
     *
     * @param name The name of the actuator.
     * @param actuator The actuator.
     */
    fun putActuator(name: Any, actuator: IButtonActuator) {
        actuators.put(name) { actuator }
    }

    /**
     * Removes an actuator from the map with the given name.
     *
     * @param name The name of the actuator.
     */
    fun removeActuator(name: Any) {
        actuators.remove(name)
    }
}

