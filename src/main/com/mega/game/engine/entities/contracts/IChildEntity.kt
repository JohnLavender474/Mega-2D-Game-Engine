package com.mega.game.engine.entities.contracts

import com.mega.game.engine.entities.GameEntity

/**
 * An GameEntity that can have a parent.
 */
interface IChildEntity {

    /**
     * The parent of this entity.
     */
    var parent: GameEntity?
}
