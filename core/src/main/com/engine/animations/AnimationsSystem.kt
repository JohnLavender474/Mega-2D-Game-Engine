package com.engine.animations

import com.engine.entities.GameEntity
import com.engine.systems.GameSystem
import com.engine.common.objects.ImmutableCollection

/** A system that can be used to animate sprites. */
class AnimationsSystem : GameSystem(AnimationsComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) {
      return
    }
    entities.forEach { entity ->
      val animationsComponent = entity.getComponent(AnimationsComponent::class)
      animationsComponent?.animations?.forEach { e ->
        val sprite = e.key
        val animator = e.value
        animator.animate(sprite, delta)
      }
    }
  }
}
