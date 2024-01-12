package com.engine.entities.contracts

import com.engine.entities.IGameEntity

/** An entity that can have children. */
interface IParentEntity : IGameEntity {

  /** The children of this entity. */
  val children: MutableCollection<IGameEntity>
}
