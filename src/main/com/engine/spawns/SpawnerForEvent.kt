package com.engine.spawns

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.engine.events.Event
import com.engine.events.IEventListener
import java.util.function.Predicate
import java.util.function.Supplier

/** Spawns an entity when an event is fired. */
class SpawnerForEvent(
    private val predicate: (Event) -> Boolean,
    private val spawnSupplier: () -> Spawn,
    override val eventKeyMask: ObjectSet<Any> = ObjectSet(),
    shouldBeCulled: (Float) -> Boolean = { false },
    onCull: () -> Unit = {},
    respawnable: Boolean = true
) : Spawner(shouldBeCulled, onCull, respawnable), IEventListener {

    private val events = Array<Event>()

    /**
     * Constructor for a [SpawnerForEvent].
     *
     * @param predicate the predicate to determine if the event should spawn
     * @param spawnSupplier the supplier for the spawn
     * @param eventKeyMask the set of event keys to listen for
     * @param shouldBeCulled the predicate to determine if the spawn should be culled
     * @param onCull the action to take when the spawn is culled
     * @param respawnable if the spawner should be considered again for spawning after the first spawn
     */
    constructor(
        predicate: Predicate<Event>,
        spawnSupplier: Supplier<Spawn>,
        eventKeyMask: ObjectSet<Any> = ObjectSet(),
        shouldBeCulled: Supplier<Boolean> = Supplier { false },
        onCull: Runnable = Runnable {},
        respawnable: Boolean = true
    ) : this(
        { predicate.test(it) },
        { spawnSupplier.get() },
        eventKeyMask,
        { shouldBeCulled.get() },
        { onCull.run() },
        respawnable
    )

    override fun test(delta: Float): Boolean {
        if (!super.test(delta)) return false

        for (event in events) if (predicate(event)) {
            spawn = spawnSupplier()
            break
        }
        events.clear()

        return spawned
    }

    override fun onEvent(event: Event) {
        if (!spawned) events.add(event)
    }

    override fun toString() = "SpawnerForEvent[eventKeyMask=$eventKeyMask]"
}
