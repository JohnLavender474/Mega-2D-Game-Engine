package com.engine.world

import com.engine.components.IGameComponent

/**
 * A component that can be used to add a body to a game entity.
 *
 * @param body the body to add to the game entity
 */
class BodyComponent(var body: Body) : IGameComponent {

  override fun reset() {
    body.reset()
  }
}
