package com.mega.game.engine.entities.contracts

import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.updatables.UpdatablesComponent


interface IUpdatableEntity : IGameEntity {


    fun getUpdatablesComponent(): UpdatablesComponent = getComponent(UpdatablesComponent::class)!!


    fun addUpdatable(updatable: Updatable) = getUpdatablesComponent().updatables.add(updatable)
}