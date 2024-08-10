package com.engine.updatables

import com.badlogic.gdx.utils.Array
import com.engine.common.interfaces.Updatable
import com.engine.components.IGameComponent

/**
 * The updatable component. Contains all the updatables for an entity.
 *
 * @param updatables The updatables
 */
class UpdatablesComponent(val updatables: Array<Updatable> = Array()) : IGameComponent {

    /**
     * The updatable component. Contains all the updatables for an entity.
     *
     * @param updatables The updatables.
     */
    constructor(vararg updatables: Updatable) : this(Array<Updatable>().apply { updatables.forEach { add(it) } })

    /**
     * Adds the given updatable to the updatables list.
     *
     * @param updatable The updatable to add.
     */
    fun add(updatable: Updatable) = updatables.add(updatable)
}
