package com.engine.damage

import com.badlogic.gdx.utils.Array
import com.engine.common.time.Timer
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A [DamageableComponent] is a [IGameComponent] that contains a [IDamageable] and an array of
 * [IDamager]s. The [IDamageable] is the object that can take damage from the [DamagerComponent]s.
 * The [DamagerComponent]s are the objects that can deal damage to the [IDamageable]. The list of
 * [IDamager]s should be cleared at the end of each update cycle.
 *
 * @param entity the [IGameEntity] this [DamageableComponent] belongs to
 * @param damageable the [IDamageable] that can take damage from the [DamagerComponent]s
 * @param damageTimer the [Timer] that keeps track of how long the [DamageableComponent] has been
 *   under damage. If the [DamageableComponent] is under damage, it cannot take damage from another
 *   [DamagerComponent]. The [DamageableComponent] is under damage until the [Timer] is finished.
 * @param damageRecoveryTimer the [Timer] that keeps track of how long the [DamageableComponent] has
 *   been recovering from damage. If the [DamageableComponent] is recovering from damage, it cannot
 *   take damage from another [DamagerComponent]. The [DamageableComponent] is recovering from
 *   damage until the [Timer] is finished. The [DamageableComponent] is invincible while it is
 *   recovering from damage. This timer is not started until the damage timer is finished.
 * @param invincible true if the [DamageableComponent] is invincible, otherwise false
 * @property damagers the [DamagerComponent]s that can deal damage to the [IDamageable]
 */
class DamageableComponent(
    override val entity: IGameEntity,
    var damageable: IDamageable,
    val damageTimer: Timer = Timer(1f),
    val damageRecoveryTimer: Timer = Timer(1f),
    var invincible: Boolean = false
) : IGameComponent {

  var damagers: Array<DamagerComponent> = Array()

  /**
   * Returns if the [DamageableComponent] can be damaged. The [DamageableComponent] can be damaged
   * if it is not invincible, not under damage, and not recovering from damage.
   *
   * @return true if the [DamageableComponent] can be damaged, otherwise false
   */
  fun canBeDamaged() = !invincible && !isUnderDamage() && !isRecoveringFromDamage()

  /**
   * Returns if the [DamageableComponent] is under damage. The [DamageableComponent] is under damage
   * if the [damageTimer] is not finished.
   *
   * @return true if the [DamageableComponent] is under damage, otherwise false
   */
  fun isUnderDamage() = !damageTimer.isFinished()

  /**
   * Updates the [damageTimer] by the given delta time.
   *
   * @param delta the delta time
   */
  fun updateDamageTimer(delta: Float) = damageTimer.update(delta)

  /**
   * Returns if the [DamageableComponent] is invincible. The [DamageableComponent] is invincible if
   * the [damageRecoveryTimer] is not finished.
   *
   * @return true if the [DamageableComponent] is invincible, otherwise false
   */
  fun isRecoveringFromDamage() = !damageRecoveryTimer.isFinished()

  /**
   * Updates the [damageRecoveryTimer] by the given delta time.
   *
   * @param delta the delta time
   */
  fun updateDamageRecoveryTimer(delta: Float) = damageRecoveryTimer.update(delta)

  /**
   * Resets the [DamageableComponent] by resetting the [damageTimer], [damageRecoveryTimer],
   * clearing the [damagers], and setting [invincible] to false.
   */
  override fun reset() {
    damageTimer.reset()
    damageRecoveryTimer.reset()
    invincible = false
    damagers.clear()
  }
}
