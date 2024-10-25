package com.mega.game.engine.entities.contracts

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.entities.IGameEntity

/**
 * An [IGameEntity] that can have children.
 */
interface IParentEntity {

    /**
     * The children of this entity.
     */
    var children: Array<IGameEntity>
}
