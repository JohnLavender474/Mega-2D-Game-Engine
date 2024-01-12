package com.engine.entities.contracts

import com.engine.entities.IGameEntity

/** An entity that can have a parent. */
interface IChildEntity : IGameEntity {

  /** The parent of this entity. */
  var parent: IGameEntity?
}
