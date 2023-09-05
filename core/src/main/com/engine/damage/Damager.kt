package com.engine.damage

/**
 * A [Damager] is an object that can deal damage to a [Damageable]. The [Damager] is responsible for
 * determining whether it can damage a [Damageable] and how much damage it deals.
 *
 * @see Damageable
 */
interface Damager {

  /**
   * Return true if this [Damager] can damage the [Damageable], otherwise false.
   *
   * @param damageable the [Damageable] that this [Damager] may be able to damage
   * @return true if this [Damager] can damage the [Damageable], otherwise false
   */
  fun canDamage(damageable: Damageable): Boolean

  /**
   * Called when this [Damager] deals damage to the [Damageable].
   *
   * @param damageable the [Damageable] that this [Damager] dealt damage to
   */
  fun onDamageInflictedTo(damageable: Damageable)
}
