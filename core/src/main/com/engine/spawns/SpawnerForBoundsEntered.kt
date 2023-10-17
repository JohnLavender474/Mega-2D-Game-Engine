package com.engine.spawns

import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.GameRectangle

/** Spawns an entity when the bounds of this spawner and the bounds of another object overlap. */
class SpawnerForBoundsEntered(
    private val spawnSupplier: () -> Spawn,
    private val thisBounds: Array<GameRectangle>,
    private val otherBounds: Array<GameRectangle>,
    shouldBeCulled: () -> Boolean = { false }
) : Spawner(shouldBeCulled) {

  constructor(
      spawnSupplier: () -> Spawn,
      thisBounds: GameRectangle,
      otherBounds: GameRectangle,
      shouldBeCulled: () -> Boolean = { false }
  ) : this(spawnSupplier, arrayOf(thisBounds), arrayOf(otherBounds), shouldBeCulled)

  override fun test(delta: Float): Boolean {
    if (!super.test(delta)) return false

    for (b1 in thisBounds) {
      for (b2 in otherBounds) {
        if (b1.overlaps(b2 as Rectangle)) {
          spawn = spawnSupplier()
          spawned = true
          return true
        }
      }
    }

    return false
  }
}
