package com.mega.game.engine.updatables

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.components.IGameComponent


class UpdatablesComponent(val updatables: Array<Updatable> = Array()) : IGameComponent {

    
    constructor(vararg updatables: Updatable) : this(Array<Updatable>().apply { updatables.forEach { add(it) } })

    
    fun add(updatable: Updatable) = updatables.add(updatable)
}
