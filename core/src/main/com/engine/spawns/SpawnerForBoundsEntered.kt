package com.engine.spawns

import com.badlogic.gdx.math.Rectangle
import com.engine.common.GameLogger
import com.engine.common.shapes.GameRectangle

/**
 * Spawns an entity when the bounds of this spawner and the bounds of another object overlap for the
 * first time.
 */
class SpawnerForBoundsEntered(
    private val spawnSupplier: () -> Spawn,
    private val thisBounds: () -> GameRectangle,
    private val otherBounds: () -> GameRectangle,
    shouldBeCulled: (Float) -> Boolean = { false },
    onCull: () -> Unit = {}
) : Spawner(shouldBeCulled, onCull) {

  companion object {
    const val TAG = "SpawnerForBoundsEntered"
  }

  private var isEntered = false

  override fun test(delta: Float): Boolean {
    if (!super.test(delta)) return false

    val wasEntered = isEntered
    isEntered = thisBounds().overlaps(otherBounds() as Rectangle)
    if (!wasEntered && isEntered) {
      spawn = spawnSupplier()
      GameLogger.debug(TAG, "test(): Spawning entity: $spawn")
    }

    return spawned
  }

  override fun reset() {
    super.reset()
    GameLogger.debug(TAG, "reset(): Resetting spawner: $this")
    isEntered = false
  }

  override fun toString() = "SpawnerForBoundsEntered"
}
