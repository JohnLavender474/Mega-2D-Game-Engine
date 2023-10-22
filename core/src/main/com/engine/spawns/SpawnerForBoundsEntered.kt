package com.engine.spawns

import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.GameRectangle

/** Spawns an entity when the bounds of this spawner and the bounds of another object overlap. */
class SpawnerForBoundsEntered(
    private val spawnSupplier: () -> Spawn,
    private val thisBounds: () -> GameRectangle,
    private val otherBounds: () -> GameRectangle,
    shouldBeCulled: () -> Boolean = { false },
    onCull: () -> Unit = {}
) : Spawner(shouldBeCulled, onCull) {

  override fun test(delta: Float): Boolean {
    if (!super.test(delta)) return false
    if (thisBounds().overlaps(otherBounds() as Rectangle)) spawn = spawnSupplier()
    return spawned
  }
}
