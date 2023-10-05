package com.engine.entities.contracts

import com.engine.damage.DamagerComponent
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity

/**
 * Convenience interface for a [GameEntity] that has a [DamagerComponent]. The [DamagerComponent]
 * should be added to this entity as a property with the key "damager".
 *
 * @see [DamagerComponent]
 */
interface DamagerEntity : IGameEntity {

  /**
   * Get the [DamagerComponent] of this [DamagerEntity]. The [DamagerComponent] should be added to
   * this entity as a property with the key "damager".
   *
   * @return the [DamagerComponent] of this [DamagerEntity]
   */
  fun getDamagerComponent() = getComponent(DamagerComponent::class)
}
