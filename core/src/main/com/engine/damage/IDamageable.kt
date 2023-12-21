package com.engine.damage

/**
 * A [IDamageable] is an object that can take damage from a [IDamager]. The [IDamageable] is
 * responsible for determining whether it can take damage from a [IDamager] and how much damage it
 * takes.
 */
interface IDamageable {

  /** Return true if this [IDamageable] is invincible, otherwise false. */
  val invincible: Boolean

  /**
   * Return true if this [IDamageable] can take damage from the [IDamager], otherwise false.
   *
   * @param damager the [IDamager] that is dealing damage to this [IDamageable]
   * @return true if this [IDamageable] can take damage from the [IDamager], otherwise false
   */
  fun canBeDamagedBy(damager: IDamager): Boolean

  /**
   * Take damage from the [IDamager]. Return true if damage was taken, otherwise false.
   *
   * @param damager the [IDamager] that is dealing damage to this [IDamageable]
   * @return true if damage was taken, otherwise false
   */
  fun takeDamageFrom(damager: IDamager): Boolean
}
