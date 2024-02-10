package com.engine.entities.contracts

import com.badlogic.gdx.utils.Array
import com.engine.entities.IGameEntity

/** An entity that can have children. */
interface IParentEntity : IGameEntity {

    /** The children of this entity. */
    var children: Array<IGameEntity>
}
