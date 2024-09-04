package com.mega.game.engine.components

import com.mega.game.engine.common.interfaces.Resettable

/** A component that can be added to a game entity. */
interface IGameComponent : Resettable {

    /**
     * Resets the component to its default state. Default implementation is a no-op.
     */
    override fun reset() {}
}
