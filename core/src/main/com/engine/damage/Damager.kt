package com.engine.damage

/** Convenience implementation of [IDamager]. */
class Damager(
    private val canDamage: ((IDamager) -> Boolean)? = null,
    private val onDamageInflictedTo: ((IDamageable) -> Unit)? = null
) : IDamager {

  override fun canDamage(damageable: IDamageable) = canDamage?.invoke(this) ?: false

  override fun onDamageInflictedTo(damageable: IDamageable) {
    onDamageInflictedTo?.invoke(damageable)
  }
}
