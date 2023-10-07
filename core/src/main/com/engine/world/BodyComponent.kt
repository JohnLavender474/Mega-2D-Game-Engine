package com.engine.world

import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A component that can be used to add a body to a game entity.
 *
 * @param body the body to add to the game entity
 */
class BodyComponent(override val entity: IGameEntity, var body: Body) : IGameComponent {

  override fun reset() {
    body.reset()
  }
}
