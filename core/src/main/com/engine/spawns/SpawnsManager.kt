package com.engine.spawns

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/**
 * Manages the spawning of entities. This class is responsible for updating all of the [Spawner]s
 * and adding the [Spawn]s to the [spawns] list. This class is also responsible for culling the
 * [Spawner]s that should no longer be considered for spawning.
 *
 * @see Updatable
 * @see Resettable
 */
class SpawnsManager : Updatable, Resettable {

  private val spawns = ArrayList<Spawn>()
  private val spawners = ArrayList<Spawner>()

  /**
   * Returns a list of the [Spawn]s that were spawned since the last update, and then clears the
   * list of spawns in this [SpawnsManager] instance. This method should be called once per frame.
   * This method should be called after the [update] method.
   *
   * @return the [Spawn]s that were spawned since the last update.
   */
  fun getSpawns(): MutableList<Spawn> {
    val spawnsToReturn = ArrayList(spawns)
    spawns.clear()
    return spawnsToReturn
  }

  /**
   * Sets the [Spawner]s to manage.
   *
   * @param spawners the [Spawner]s to manage.
   * @see Spawner
   */
  fun setSpawners(spawners: Collection<Spawner>) {
    this.spawners.clear()
    this.spawners.addAll(spawners)
  }

  /**
   * Updates the [Spawner]s and adds the [Spawn]s to the [spawns] list. Also culls the [Spawner]s
   * that should no longer be considered for spawning. This method should be called once per frame.
   * This method should be called before the [getSpawns] method.
   *
   * @param delta the time in seconds since the last update.
   */
  override fun update(delta: Float) {
    val iter = spawners.iterator()

    while (iter.hasNext()) {
      val spawner = iter.next()

      if (spawner.shouldBeCulled()) {
        iter.remove()
        continue
      }

      if (spawner.test(delta)) {
        spawns.add(spawner.get())
      }
    }
  }

  /** Clears the [Spawner]s and [Spawn]s. */
  override fun reset() {
    spawners.clear()
    spawns.clear()
  }
}
