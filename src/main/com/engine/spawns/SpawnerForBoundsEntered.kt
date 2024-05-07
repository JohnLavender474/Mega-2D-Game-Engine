package com.engine.spawns

import com.engine.common.shapes.IGameShape2D

/**
 * Spawns an entity when the shape of this spawner and the shape of another object overlap for the
 * first time.
 */
class SpawnerForBoundsEntered(
    private val spawnSupplier: () -> Spawn,
    private val thisBounds: () -> IGameShape2D,
    private val otherBounds: () -> IGameShape2D,
    shouldBeCulled: (Float) -> Boolean = { false },
    onCull: () -> Unit = {},
    respawnable: Boolean = true
) : Spawner(shouldBeCulled, onCull, respawnable) {

    companion object {
        const val TAG = "SpawnerForBoundsEntered"
    }

    private var isEntered = false

    override fun test(delta: Float): Boolean {
        if (!super.test(delta)) return false

        val wasEntered = isEntered
        isEntered = thisBounds().overlaps(otherBounds())
        if (!wasEntered && isEntered) spawn = spawnSupplier()

        return spawned
    }

    override fun reset() {
        super.reset()
        isEntered = false
    }

    override fun toString() = TAG
}
