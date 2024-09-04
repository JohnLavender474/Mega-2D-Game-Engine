package com.mega.game.engine.cullables

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.components.IGameComponent

/**
 * A component that holds a list of [ICullable]s.
 *
 * @param cullables The list of [ICullable]s.
 */
class CullablesComponent(val cullables: ObjectMap<String, ICullable> = ObjectMap()) : IGameComponent {

    /**
     * Adds a [ICullable] to the list of cullables.
     *
     * @param cullable The cullable to add.
     */
    fun put(key: String, cullable: ICullable) {
        cullables.put(key, cullable)
    }

    /**
     * Removes a [ICullable] from the list of cullables.
     *
     * @param key The key of the cullable to remove.
     */
    fun remove(key: String) {
        cullables.remove(key)
    }

    override fun reset() = cullables.values().forEach { it.reset() }
}
