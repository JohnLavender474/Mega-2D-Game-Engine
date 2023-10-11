package com.engine.spawns

import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.GameRectangle

class SpawnerForBoundsEntered(
    private val spawnSupplier: () -> Spawn,
    private val thisBounds: Array<GameRectangle>,
    private val otherBounds: Array<GameRectangle>,
    private val shouldBeCulled: () -> Boolean = { false }
) : ISpawner {

  constructor(
      spawnSupplier: () -> Spawn,
      thisBounds: GameRectangle,
      otherBounds: GameRectangle,
      shouldBeCulled: () -> Boolean = { false }
  ) : this(spawnSupplier, arrayOf(thisBounds), arrayOf(otherBounds), shouldBeCulled)

  private var spawned = false
  private var spawn: Spawn? = null

  override fun shouldBeCulled() = shouldBeCulled.invoke()

  override fun get() = spawn

  override fun test(delta: Float): Boolean {
    if (spawn?.entity?.dead == true) {
      spawn = null
      spawned = false
    }

    if (spawned) return false

    for (b1 in thisBounds) {
      for (b2 in otherBounds) {
        if (b1.overlaps(b2 as Rectangle)) {
          spawn = spawnSupplier()
          return true
        }
      }
    }

    return false
  }
}
