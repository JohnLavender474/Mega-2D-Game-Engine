package com.engine.damage

/**
 * Convenience implementation of [IDamageable]. If [_takeDamageFrom] is null, then [takeDamageFrom]
 * will always do nothing and return false.
 *
 * @param _takeDamageFrom the function that determines whether this [Damageable] can take damage
 *   from the [IDamager
 */
class Damageable(private val _takeDamageFrom: ((IDamager) -> Boolean)? = null) : IDamageable {

  override fun takeDamageFrom(damager: IDamager) = _takeDamageFrom?.invoke(damager) ?: false
}
