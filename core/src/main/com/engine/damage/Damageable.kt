package com.engine.damage

/**
 * A [Damageable] is an object that can take damage from a [Damager]. The [Damageable] is
 * responsible for determining whether it can take damage from a [Damager] and how much damage it
 * takes.
 */
fun interface Damageable {

  /**
   * Take damage from the [Damager]. Return true if damage was taken, otherwise false.
   *
   * @param damager the [Damager] that is dealing damage to this [Damageable]
   * @return true if damage was taken, otherwise false
   */
  fun takeDamageFrom(damager: Damager): Boolean
}
