package com.engine.damage

import com.engine.common.objects.Properties

/** Convenience implementation of [IDamageable]. */
class Damageable(
    private val takeDamageFrom: ((IDamager) -> Boolean)? = null,
    override val properties: Properties = Properties()
) : IDamageable {

  override fun takeDamageFrom(damager: IDamager) = takeDamageFrom?.invoke(damager) ?: false
}
