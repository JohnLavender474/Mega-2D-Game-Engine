package com.engine.spawns

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.engine.events.Event
import com.engine.events.IEventListener

class SpawnerForEvent(
    private val predicate: (Event) -> Boolean,
    private val spawnSupplier: () -> Spawn,
    shouldBeCulled: () -> Boolean = { false },
    override val eventKeyMask: ObjectSet<Any> = ObjectSet()
) : Spawner(shouldBeCulled), IEventListener {

  private val events = Array<Event>()

  override fun test(delta: Float): Boolean {
    if (!super.test(delta)) return false

    val _events = Array(events)
    events.clear()

    _events.forEach {
      if (predicate(it)) {
        spawn = spawnSupplier()
        spawned = true
        return true
      }
    }

    return false
  }

  override fun onEvent(event: Event) {
    if (!spawned) events.add(event)
  }
}
