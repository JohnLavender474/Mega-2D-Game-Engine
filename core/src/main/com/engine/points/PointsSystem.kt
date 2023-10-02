package com.engine.points

import com.engine.entities.GameEntity
import com.engine.systems.GameSystem
import com.engine.common.objects.ImmutableCollection

/** The pointsHandles system. Processes all the stats of each [GameEntity]. */
class PointsSystem : GameSystem(PointsComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) return

    entities.forEach { entity ->
      val pointsComponent = entity.getComponent(PointsComponent::class)

      pointsComponent?.pointsHandles?.forEach {
        val (points, pointsListener) = it
        pointsListener(points)
      }
    }
  }
}
