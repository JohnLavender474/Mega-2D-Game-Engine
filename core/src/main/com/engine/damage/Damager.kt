package com.engine.damage

/**
 * Convenience implementation of [IDamager].
 *
 * @param canDamage the function that determines whether this [Damager] can damage the [IDamageable]
 * @param onDamageInflictedTo the function that is called when this [Damager] deals damage to the
 */
class Damager(
    private val canDamage: ((IDamager) -> Boolean)? = null,
    private val onDamageInflictedTo: ((IDamageable) -> Unit)? = null
) : IDamager {

  override fun canDamage(damageable: IDamageable) = canDamage?.invoke(this) ?: false

  override fun onDamageInflictedTo(damageable: IDamageable) {
    onDamageInflictedTo?.invoke(damageable)
  }
}
