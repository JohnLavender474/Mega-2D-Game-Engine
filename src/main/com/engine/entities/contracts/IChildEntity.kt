package com.engine.entities.contracts

import com.engine.entities.IGameEntity

/**
 * An IGameEntity that can have a parent.
 */
interface IChildEntity {

    /**
     * The parent of this entity.
     */
    var parent: IGameEntity?
}
