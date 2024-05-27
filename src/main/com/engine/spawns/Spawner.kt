package com.engine.spawns

import com.engine.common.GameLogger
import java.util.function.Predicate

/** A [Spawner] is used to spawn an entity. */
abstract class Spawner(
    protected val shouldBeCulled: (Float) -> Boolean = { false },
    protected val onCull: () -> Unit = {},
    override var respawnable: Boolean = true
) : ISpawner {

    companion object {
        const val TAG = "Spawner"
    }

    val spawned: Boolean
        get() = spawn != null

    protected var spawn: Spawn? = null

    /**
     * Constructor for a [Spawner].
     *
     * @param shouldBeCulled the predicate to determine if the spawn should be culled
     * @param onCull the action to take when the spawn is culled
     * @param respawnable if the spawner should be considered again for spawning after the first spawn
     */
    constructor(
        shouldBeCulled: Predicate<Float> = Predicate { false },
        onCull: Runnable = Runnable {},
        respawnable: Boolean = true
    ) : this (
        shouldBeCulled = shouldBeCulled::test,
        onCull = onCull::run,
        respawnable = respawnable
    )

    override fun get(): Spawn? {
        GameLogger.debug(TAG, "get(): Spawning entity: $spawn")
        return spawn
    }

    override fun test(delta: Float): Boolean {
        if (spawn?.entity?.dead == true) spawn = null
        return !spawned
    }

    override fun shouldBeCulled(delta: Float) = shouldBeCulled.invoke(delta)

    override fun reset() = onCull()
}
