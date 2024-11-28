package com.mega.game.engine.entities.contracts

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.entities.IGameEntity

interface IParentEntity<T: IGameEntity> {

    var children: Array<T>
}
