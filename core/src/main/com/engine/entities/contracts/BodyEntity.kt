package com.engine.entities.contracts

import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
import com.engine.world.BodyComponent

/**
 * Convenience interface for a [GameEntity] that has a [BodyComponent]. The [BodyComponent] should
 * be added to this entity as a property with the key "BodyComponent".
 *
 * @see [BodyComponent]
 */
interface BodyEntity : IGameEntity {

  /**
   * Get the [BodyComponent] of this [BodyEntity]. The [BodyComponent] should be added to this
   * entity as a property with the key "body_component".
   *
   * @return the [BodyComponent] of this [BodyEntity]
   */
  fun getBodyComponent() = getComponent(BodyComponent::class)
}
