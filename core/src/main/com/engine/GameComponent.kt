package com.engine

import com.engine.common.interfaces.Resettable

/** A component that can be added to a game entity. */
interface GameComponent : Resettable {

  override fun reset() {}
}
