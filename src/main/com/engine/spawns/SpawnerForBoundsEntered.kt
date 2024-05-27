package com.engine.spawns

import com.engine.common.shapes.IGameShape2D
import java.util.function.Supplier

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

    /**
     * Constructor for a [SpawnerForBoundsEntered].
     *
     * @param spawnSupplier the supplier for the spawn
     * @param thisBounds the supplier for the bounds of this spawner
     * @param otherBounds the supplier for the bounds of the other object
     * @param shouldBeCulled the predicate to determine if the spawn should be culled
     * @param onCull the action to take when the spawn is culled
     * @param respawnable if the spawner should be considered again for spawning after the first spawn
     */
    constructor(
        spawnSupplier: Supplier<Spawn>,
        thisBounds: Supplier<IGameShape2D>,
        otherBounds: Supplier<IGameShape2D>,
        shouldBeCulled: Supplier<Boolean> = Supplier { false },
        onCull: Runnable = Runnable {},
        respawnable: Boolean = true
    ) : this(
        { spawnSupplier.get() },
        { thisBounds.get() },
        { otherBounds.get() },
        { shouldBeCulled.get() },
        { onCull.run() },
        respawnable = respawnable
    )

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
