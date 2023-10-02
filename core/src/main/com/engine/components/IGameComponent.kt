package com.engine.components

import com.engine.common.interfaces.Resettable

/** A component that can be added to a game entity. */
interface IGameComponent : Resettable {

  override fun reset() {}
}
