package com.engine.entities.contracts

import com.engine.cullables.CullablesComponent
import com.engine.entities.IGameEntity

/** An [IGameEntity] that can be culled. */
interface ICullableEntity : IGameEntity {

    /**
     * Returns the [CullablesComponent] of this entity.
     *
     * @return the [CullablesComponent] of this entity
     */
    fun getCullablesComponent() = getComponent(CullablesComponent::class)!!
}
