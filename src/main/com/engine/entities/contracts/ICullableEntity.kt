package com.engine.entities.contracts

import com.engine.cullables.CullablesComponent
import com.engine.cullables.ICullable
import com.engine.entities.IGameEntity

/** An [IGameEntity] that can be culled. */
interface ICullableEntity : IGameEntity {

    /**
     * Returns the [CullablesComponent] of this entity.
     *
     * @return the [CullablesComponent] of this entity
     */
    fun getCullablesComponent() = getComponent(CullablesComponent::class)!!

    /**
     * Adds a [ICullable] to the list of cullables.
     *
     * @param key The key of the cullable.
     * @param cullable The cullable to add.
     */
    fun putCullable(key: String, cullable: ICullable) = getCullablesComponent().put(key, cullable)

    /**
     * Removes a [ICullable] from the list of cullables.
     *
     * @param key The key of the cullable to remove.
     */
    fun removeCullable(key: String) = getCullablesComponent().remove(key)
}
