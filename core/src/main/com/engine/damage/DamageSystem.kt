package com.engine.damage

import com.engine.entities.GameEntity
import com.engine.systems.GameSystem
import com.engine.common.objects.ImmutableCollection

/** A [DamageSystem] is a [GameSystem] that processes [DamageComponent]s. */
class DamageSystem : GameSystem(DamageComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) return

    entities.forEach { entity ->
      entity.getComponent(DamageComponent::class)?.let { damageComponent ->
        val damagers = damageComponent.damagers
        val damageable = damageComponent.damageable

        damagers.forEach { damager ->
          if (damager.canDamage(damageable) && damageable.takeDamageFrom(damager)) {
            damager.onDamageInflictedTo(damageable)
          }
        }

        damagers.clear()
      }
    }
  }
}
