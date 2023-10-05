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
        damageComponent.updateDamageTimer(delta)
        if (damageComponent.damageTimer.isFinished()) {
          damageComponent.updateDamageRecoveryTimer(delta)
        }

        if (!damageComponent.canBeDamaged()) {
          return
        }

        val damagers = damageComponent.damagers
        val damageable = damageComponent.damageable

        damagers
            .map { it.damager }
            .forEach { damager ->
              if (damager.canDamage(damageable) && damageable.takeDamageFrom(damager)) {
                damager.onDamageInflictedTo(damageable)
              }
            }
        damagers.clear()
      }
    }
  }
}
