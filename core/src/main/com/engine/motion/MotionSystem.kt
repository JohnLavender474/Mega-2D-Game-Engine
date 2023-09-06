package com.engine.motion

import com.engine.GameEntity
import com.engine.GameSystem
import com.engine.common.objects.ImmutableCollection

/** A system that updates the [MotionComponent]s of [GameEntity]s. */
class MotionSystem : GameSystem(MotionComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) return

    entities.forEach {
      it.getComponent(MotionComponent::class)?.let { motionComponent ->
        motionComponent.motions.forEach { (motion, function) ->
          motion.update(delta)
          motion.getMotionValue()?.let { value -> function(value) }
        }
      }
    }
  }
}
