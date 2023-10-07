package com.engine.components

import com.engine.common.interfaces.Resettable
import com.engine.entities.IGameEntity

/** A component that can be added to a game entity. */
interface IGameComponent : Resettable {

  /** The [IGameEntity] this [IGameComponent] belongs to. */
  val entity: IGameEntity

  override fun reset() {}
}
