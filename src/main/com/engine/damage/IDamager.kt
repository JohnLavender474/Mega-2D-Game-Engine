package com.engine.damage

/**
 * A [IDamager] is an object that can deal damage to a [IDamageable].
 *
 * @see IDamageable
 */
interface IDamager {

    /**
     * Return true if this [IDamager] can deal damage to the [IDamageable], otherwise false.
     *
     * @param damageable the [IDamageable] that is being damaged by this [IDamager]
     * @return true if this [IDamager] can deal damage to the [IDamageable], otherwise false
     */
    fun canDamage(damageable: IDamageable): Boolean

    /**
     * Called when this [IDamager] deals damage to the [IDamageable].
     *
     * @param damageable the [IDamageable] that is being damaged by this [IDamager]
     */
    fun onDamageInflictedTo(damageable: IDamageable)
}
