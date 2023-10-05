package com.engine.damage

/** Convenience implementation of [IDamageable]. */
class Damageable(private val takeDamageFrom: ((IDamager) -> Boolean)? = null) : IDamageable {

  override fun takeDamageFrom(damager: IDamager) = takeDamageFrom?.invoke(damager) ?: false
}
