package com.engine.spawns

import com.badlogic.gdx.utils.Array
import com.engine.events.Event
import com.engine.events.IEventListener

class SpawnerForEvent(
    private val predicate: (Event) -> Boolean,
    private val spawnSupplier: () -> Spawn,
    private val shouldBeCulled: () -> Boolean = { false }
) : ISpawner, IEventListener {

  private val events = Array<Event>()
  private var spawned = false
  private var spawn: Spawn? = null

  override fun get() = spawn

  override fun test(delta: Float): Boolean {
    if (spawn?.entity?.dead == true) {
      spawn = null
      spawned = false
    }

    if (spawned) return false

    events.forEach {
      if (predicate(it)) {
        spawn = spawnSupplier()
        return true
      }
    }
    events.clear()

    return false
  }

  override fun shouldBeCulled() = shouldBeCulled.invoke()

  override fun onEvent(event: Event) {
    if (!spawned) events.add(event)
  }
}
