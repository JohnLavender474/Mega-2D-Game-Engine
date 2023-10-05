package com.engine.cullables

import com.engine.GameEngine
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A [GameSystem] that culls [GameEntity]s with [CullablesComponent]s. Culled entities are marked as
 * dead. This means they will not be destroyed until the end of the [GameEngine]'s update cycle.
 *
 * @see CullablesComponent
 */
class CullablesSystem : GameSystem(CullablesComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    entities.forEach { entity ->
      if (entity.dead) {
        return
      }

      val cullables = entity.getComponent(CullablesComponent::class)?.cullables
      val shouldBeCulled = cullables?.any { it.shouldBeCulled() }

      if (shouldBeCulled == true) {
        entity.dead = true
      }
    }
  }
}
