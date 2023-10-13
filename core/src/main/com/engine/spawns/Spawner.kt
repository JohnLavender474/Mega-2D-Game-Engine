package com.engine.spawns

abstract class Spawner(protected val shouldBeCulled: () -> Boolean = { false }) : ISpawner {

  protected var spawned = false
  protected var spawn: Spawn? = null

  override fun get() = spawn

  override fun test(delta: Float): Boolean {
    if (spawn?.entity?.dead == true) {
      spawn = null
      spawned = false
    }
    if (spawned) return false
    return true
  }

  override fun shouldBeCulled() = shouldBeCulled.invoke()
}
