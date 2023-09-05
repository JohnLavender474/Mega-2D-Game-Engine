package com.engine.updatables

import com.engine.GameEntity
import com.engine.GameSystem
import com.engine.common.objects.ImmutableCollection

/** The updatable system. Processes all the updatables of each [GameEntity]. */
class UpdatableSystem : GameSystem(UpdatableComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) return

    entities.forEach { entity ->
      val updatableComponent = entity.getComponent(UpdatableComponent::class)
      updatableComponent?.updatables?.forEach { it.update(delta) }
    }
  }
}
