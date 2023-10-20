package com.engine.points

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/** The pointsMap system. Processes all the pointsMap of each [GameEntity]. */
class PointsSystem : GameSystem(PointsComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    entities.forEach { entity ->
      val pointsComponent = entity.getComponent(PointsComponent::class)

      pointsComponent?.pointsMap?.values()?.forEach {
        val (points, pointsListener) = it
        pointsListener?.invoke(points)
      }
    }
  }
}
