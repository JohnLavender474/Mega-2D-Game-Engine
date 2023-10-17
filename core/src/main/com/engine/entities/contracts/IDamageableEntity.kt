package com.engine.entities.contracts

import com.engine.damage.DamageableComponent
import com.engine.entities.IGameEntity

/** An entity that can be damaged. */
interface IDamageableEntity : IGameEntity {

  /** Sets the [DamageableComponent.invincible] of this entity. */
  var invincible: Boolean
    get() = getDamageableComponent().invincible
    set(value) {
      getDamageableComponent().invincible = value
    }

  /**
   * Returns the [DamageableComponent] of this entity.
   *
   * @return the [DamageableComponent] of this entity
   */
  fun getDamageableComponent() = getComponent(DamageableComponent::class)!!

  /**
   * Returns if the [DamageableComponent] can be damaged. The [DamageableComponent] can be damaged
   * if it is not invincible, not under damage, and not recovering from damage.
   *
   * @return true if the [DamageableComponent] can be damaged, otherwise false
   */
  fun canBeDamaged() = getDamageableComponent().canBeDamaged()

  /**
   * Returns if the [DamageableComponent] is under damage. The [DamageableComponent] is under damage
   * if the [DamageableComponent.damageTimer] is not finished.
   *
   * @return true if the [DamageableComponent] is under damage, otherwise false
   */
  fun isUnderDamage() = getDamageableComponent().isUnderDamage()

  /**
   * Returns if the [DamageableComponent] is recovering from damage. The [DamageableComponent] is
   * recovering from damage if the [DamageableComponent.damageRecoveryTimer] is not finished.
   *
   * @return true if the [DamageableComponent] is recovering from damage, otherwise false
   */
  fun isRecoveringFromDamage() = getDamageableComponent().isRecoveringFromDamage()
}
