package com.engine.damage

import com.engine.common.interfaces.IPropertizable

/**
 * A [IDamager] is an object that can deal damage to a [IDamageable]. The [IDamager] is responsible for
 * determining whether it can damage a [IDamageable] and how much damage it deals.
 *
 * @see IDamageable
 */
interface IDamager: IPropertizable {

  /**
   * Return true if this [IDamager] can damage the [IDamageable], otherwise false.
   *
   * @param damageable the [IDamageable] that this [IDamager] may be able to damage
   * @return true if this [IDamager] can damage the [IDamageable], otherwise false
   */
  fun canDamage(damageable: IDamageable): Boolean

  /**
   * Called when this [IDamager] deals damage to the [IDamageable].
   *
   * @param damageable the [IDamageable] that this [IDamager] dealt damage to
   */
  fun onDamageInflictedTo(damageable: IDamageable) {}
}
