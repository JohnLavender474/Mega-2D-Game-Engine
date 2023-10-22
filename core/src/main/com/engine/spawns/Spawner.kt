package com.engine.spawns

import com.badlogic.gdx.Gdx

/** A [Spawner] is used to spawn an entity. */
abstract class Spawner(
    protected val shouldBeCulled: () -> Boolean = { false },
    protected val onCull: () -> Unit = {}
) : ISpawner {

  companion object {
    const val TAG = "Spawner"
  }

  val spawned: Boolean
    get() = spawn != null

  protected var spawn: Spawn? = null

  override fun get(): Spawn? {
    Gdx.app.debug(TAG, "get(): Spawning entity: $spawn")
    return spawn
  }

  override fun test(delta: Float): Boolean {
    if (spawn?.entity?.dead == true) spawn = null
    return !spawned
  }

  override fun shouldBeCulled() = shouldBeCulled.invoke()

  override fun reset() = onCull()
}
