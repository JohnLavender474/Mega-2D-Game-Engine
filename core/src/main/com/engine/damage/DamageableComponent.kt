package com.engine.damage

import com.badlogic.gdx.utils.Array
import com.engine.common.time.Timer
import com.engine.components.IGameComponent

/**
 * A [DamageableComponent] is a [IGameComponent] that contains a [Damageable] and an array of
 * [Damager]s. The [Damageable] is the object that can take damage from the [DamagerComponent]s. The
 * [DamagerComponent]s are the objects that can deal damage to the [Damageable]. The list of
 * [Damager]s should be cleared at the end of each update cycle.
 *
 * @param damageable the [Damageable] that can take damage from the [DamagerComponent]s
 * @property damagers the [DamagerComponent]s that can deal damage to the [Damageable]
 */
class DamageableComponent(
    internal var damageable: Damageable,
    val damageTimer: Timer = Timer(1f),
    val damageRecoveryTimer: Timer = Timer(1f),
    var invincible: Boolean = false
) : IGameComponent {

  var damagers: Array<DamagerComponent> = Array()

  fun canBeDamaged() = !invincible && !isUnderDamage() && !isRecoveringFromDamage()

  fun isUnderDamage() = !damageTimer.isFinished()

  fun updateDamageTimer(delta: Float) = damageTimer.update(delta)

  fun isRecoveringFromDamage() = !damageRecoveryTimer.isFinished()

  fun updateDamageRecoveryTimer(delta: Float) = damageRecoveryTimer.update(delta)

  override fun reset() {
    damageTimer.reset()
    damageRecoveryTimer.reset()
    invincible = false
    damagers.clear()
  }
}
