package com.engine.damage

import com.engine.common.objects.Properties

/** Convenience implementation of [IDamager]. */
class Damager(
    private val canDamage: ((IDamager) -> Boolean)? = null,
    private val onDamageInflictedTo: ((IDamageable) -> Unit)? = null,
    override val properties: Properties = Properties()
) : IDamager {

  override fun canDamage(damageable: IDamageable) = canDamage?.invoke(this) ?: false

  override fun onDamageInflictedTo(damageable: IDamageable) {
    onDamageInflictedTo?.invoke(damageable)
  }
}
