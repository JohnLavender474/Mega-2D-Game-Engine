package com.engine.spawns

import com.engine.common.interfaces.UpdatePredicate
import com.engine.cullables.ICullable

/**
 * Acts as the spawner of an entity. If the [test] method returns true, then the [get] method can be
 * called to retrieve the [Spawn]. If the [shouldBeCulled] method returns true, then this [Spawn]
 * should no longer be considered for spawning.
 *
 * @see UpdatePredicate
 * @see ICullable
 * @see Spawn
 */
interface ISpawner : UpdatePredicate, ICullable {

  /**
   * Gets the [Spawn] if the [test] method returns true.
   *
   * @return the [Spawn] if the [test] method returns true.
   */
  fun get(): Spawn?

  /**
   * Returns true if the [Spawn] should be culled. If this method returns true, then this [Spawn].
   * By default, returns false always
   *
   * @return true if the [Spawn] should be culled.
   */
  override fun shouldBeCulled() = false
}
