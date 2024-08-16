package com.engine.entities.contracts

import com.engine.common.interfaces.Updatable
import com.engine.entities.IGameEntity
import com.engine.updatables.UpdatablesComponent

/**
 * An [IUpdatableEntity] is an [IGameEntity] that can be updated. It has an [UpdatablesComponent] that holds
 * [Updatable] objects that can be updated.
 */
interface IUpdatableEntity : IGameEntity {

    /**
     * Gets the [UpdatablesComponent] for this [IUpdatableEntity].
     *
     * @return The [UpdatablesComponent] for this [IUpdatableEntity].
     */
    fun getUpdatablesComponent(): UpdatablesComponent = getComponent(UpdatablesComponent::class)!!

    /**
     * Adds the specified [Updatable] to the [UpdatablesComponent] for this [IUpdatableEntity].
     *
     * @param updatable The [Updatable] to add.
     */
    fun addUpdatable(updatable: Updatable) = getUpdatablesComponent().updatables.add(updatable)
}