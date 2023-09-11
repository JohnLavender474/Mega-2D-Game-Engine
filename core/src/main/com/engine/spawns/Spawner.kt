package com.engine.spawns

import com.engine.common.interfaces.UpdatePredicate
import com.engine.cullables.Cullable

/**
 * Acts as the spawner of an entity. If the [test] method returns true, then the [get] method can be
 * called to retrieve the [Spawn]. If the [shouldBeCulled] method returns true, then this [Spawn]
 * should no longer be considered for spawning.
 *
 * @see UpdatePredicate
 * @see Cullable
 * @see Spawn
 */
interface Spawner : UpdatePredicate, Cullable {

  /**
   * Gets the [Spawn] if the [test] method returns true.
   *
   * @return the [Spawn] if the [test] method returns true.
   */
  fun get(): Spawn
}
