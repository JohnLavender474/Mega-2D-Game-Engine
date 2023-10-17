package com.engine.animations

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/** A system that can be used to animate sprites. */
class AnimationsSystem : GameSystem(AnimationsComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) {
      return
    }
    entities.forEach { entity ->
      val animationsComponent = entity.getComponent(AnimationsComponent::class)
      animationsComponent?.animations?.forEach { e ->
        val sprite = e.first
        val animator = e.second
        animator.animate(sprite, delta)
      }
    }
  }
}
