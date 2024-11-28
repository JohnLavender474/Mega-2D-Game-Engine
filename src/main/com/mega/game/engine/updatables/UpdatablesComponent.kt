package com.mega.game.engine.updatables

import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.components.IGameComponent

class UpdatablesComponent(val updatables: OrderedMap<Any, Updatable> = OrderedMap()) : IGameComponent {

    fun put(key: Any, updatable: Updatable): Updatable? = updatables.put(key, updatable)

    fun remove(key: Any): Updatable? = updatables.remove(key)

    fun contains(key: Any) = updatables.containsKey(key)
}
