package com.engine.behaviors

import com.engine.GameEntity
import com.engine.GameSystem
import com.engine.common.objects.ImmutableCollection

/** A [GameSystem] that processes [BehaviorsComponent]s. */
class BehaviorsSystem : GameSystem(BehaviorsComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) {
      return
    }

    entities.forEach { entity ->
      entity.getComponent(BehaviorsComponent::class)?.let { b ->
        b.behaviors.forEach { (key, behavior) ->
          behavior.update(delta)
          val active = behavior.isActive()
          b.setActive(key, active)
        }
      }
    }
  }
}
