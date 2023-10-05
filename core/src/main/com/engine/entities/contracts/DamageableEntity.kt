package com.engine.entities.contracts

import com.engine.damage.DamageableComponent
import com.engine.damage.DamagerComponent
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity

/**
 * Convenience interface for a [GameEntity] that has a [DamageableComponent]. The
 * [DamageableComponent] should be added to this entity as a property with the key "damageable".
 *
 * @see [DamageableComponent]
 */
interface DamageableEntity : IGameEntity {

  /**
   * Get the [DamageableComponent] of this [DamageableEntity]. The [DamageableComponent] should be
   * added to this entity as a property with the key "damageable".
   *
   * @return the [DamageableComponent] of this [DamageableEntity]
   */
  fun getDamageable() = getComponent(DamagerComponent::class)
}
