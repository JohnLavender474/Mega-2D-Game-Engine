package com.mega.game.engine.world

import com.mega.game.engine.components.IGameComponent

/**
 * A component that can be used to add a body to a game entity.
 *
 * @param body the body to add to the game entity
 */
class BodyComponent(var body: Body) : IGameComponent {

    /**
     * Resets the body.
     */
    override fun reset() = body.reset()
}
