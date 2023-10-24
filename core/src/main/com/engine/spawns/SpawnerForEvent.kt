package com.engine.spawns

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.engine.events.Event
import com.engine.events.IEventListener

/** Spawns an entity when an event is fired. */
class SpawnerForEvent(
    private val predicate: (Event) -> Boolean,
    private val spawnSupplier: () -> Spawn,
    override val eventKeyMask: ObjectSet<Any> = ObjectSet(),
    shouldBeCulled: () -> Boolean = { false },
    onCull: () -> Unit = {},
) : Spawner(shouldBeCulled, onCull), IEventListener {

  private val events = Array<Event>()

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
