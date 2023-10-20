package com.engine.damage

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/** A [DamageSystem] is a [GameSystem] that processes [DamageableComponent]s. */
class DamageSystem : GameSystem(DamageableComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    entities.forEach { entity ->
      entity.getComponent(DamageableComponent::class)?.let { damageComponent ->
        // update the damage timer
        damageComponent.updateDamageTimer(delta)

        // if the damage timer is finished, then update the damage recovery timer
        if (damageComponent.damageTimer.isFinished())
            damageComponent.updateDamageRecoveryTimer(delta)

        // fetch the damagers added to the damage component
        val damagers = damageComponent.damagers

        // if the damageable can be damaged, then apply damage from the first damager
        if (damageComponent.canBeDamaged()) {
          val damageable = damageComponent.damageable

          for (damager in damagers) if (damager.canDamage(damageable) &&
              damageable.takeDamageFrom(damager)) {
            // if damage is inflicted, then reset the damage and recovery timers and break
            // out of the loop because the damageable can only be damaged once per frame
            damager.onDamageInflictedTo(damageable)
            damageComponent.damageTimer.reset()
            damageComponent.damageRecoveryTimer.reset()
            break
          }
        }

        // clear the damagers
        damagers.clear()
      }
    }
  }
}
