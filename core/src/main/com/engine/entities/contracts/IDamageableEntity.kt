package com.engine.entities.contracts

import com.engine.damage.DamageableComponent
import com.engine.damage.IDamageable
import com.engine.damage.IDamager
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
   * Returns the [IDamageable] of this entity.
   *
   * @return the [IDamageable] of this entity
   */
  fun getDamageable() = getDamageableComponent().damageable

  /**
   * Returns if this entity is under damage.
   *
   * @return true if this entity is under damage, otherwise false
   */
  fun isUnderDamage() = getDamageableComponent().isUnderDamage()

  /**
   * Returns if this entity is invincible.
   *
   * @return true if this entity is invincible, otherwise false
   */
  fun isInvincible() = getDamageableComponent().invincible

  /**
   * Returns if this entity can be damaged.
   *
   * @return true if this entity can be damaged, otherwise false
   */
  fun canBeDamaged() = getDamageableComponent().canBeDamaged()

  /**
   * Returns if this entity is recovering from damage.
   *
   * @return true if this entity is recovering from damage, otherwise false
   */
  fun isRecoveringFromDamage() = getDamageableComponent().isRecoveringFromDamage()

  /**
   * Adds the given [damager] to this entity's [DamageableComponent].
   *
   * @param damager the damager
   */
  fun addDamager(damager: IDamager) = getDamageableComponent().damagers.add(damager)
}
