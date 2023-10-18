package com.engine.entities.contracts

import com.engine.damage.DamagerComponent
import com.engine.damage.IDamager
import com.engine.entities.IGameEntity

/** An entity that can damage other entities. */
interface IDamagerEntity : IGameEntity {

  /**
   * Returns the [DamagerComponent] of this entity.
   *
   * @return the [DamagerComponent] of this entity
   */
  fun getDamagerComponent() = getComponent(DamagerComponent::class)!!

  /**
   * Returns the [IDamager] of this entity.
   *
   * @return the [IDamager] of this entity
   */
  fun getDamager() = getDamagerComponent().damager
}
