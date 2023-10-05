package com.engine.behaviors

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/** A [GameSystem] that processes [BehaviorsComponent]s. */
class BehaviorsSystem : GameSystem(BehaviorsComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) {
      return
    }

    entities.forEach { entity ->
      entity.getComponent(BehaviorsComponent::class)?.let { b ->
        b.behaviors.forEach { e ->
          val key = e.key
          val behavior = e.value

          behavior.update(delta)

          val active = behavior.isActive()
          b.setActive(key, active)
        }
      }
    }
  }
}
