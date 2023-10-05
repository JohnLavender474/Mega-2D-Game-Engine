package com.engine.systems

import com.engine.common.interfaces.Activatable
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.entities.IGameEntity

/** An [IGameSystem] is an [Updatable] that processes [IGameEntity]s. */
interface IGameSystem : Activatable, Updatable, Resettable {

  /** Purges all [IGameEntity]s from this [IGameSystem]. */
  fun purge()

  /**
   * Returns whether this [IGameSystem] contains the given [IGameEntity].
   *
   * @param e the [IGameEntity] to check
   * @return whether this [GameSystem] contains the given [IGameEntity]
   */
  fun contains(e: IGameEntity): Boolean

  /**
   * Removes the given [IGameEntity] from this [IGameSystem].
   *
   * @param e the [IGameEntity] to remove
   * @return whether the [IGameEntity] was removed
   */
  fun remove(e: IGameEntity): Boolean

  /**
   * Adds the given [IGameEntity] to this [IGameSystem] if it qualifies.
   *
   * @param e the [IGameEntity] to add
   * @return whether the [IGameEntity] was added
   */
  fun add(e: IGameEntity): Boolean

  /** @see addAll */
  fun addAll(vararg entities: IGameEntity): Collection<IGameEntity>

  /**
   * Adds all the given [IGameEntity]s to this [GameSystem] if they qualify.
   *
   * @param entities the [Iterable] of [IGameEntity]s to add
   * @return the [IGameEntity]s that could not be added
   */
  fun addAll(entities: Iterable<IGameEntity>): Collection<IGameEntity>

  /**
   * Returns whether the given [IGameEntity] qualifies to be added to this [IGameSystem].
   *
   * @param e the [IGameEntity] to check
   * @return whether the [IGameEntity] qualifies
   */
  fun qualifies(e: IGameEntity): Boolean
}
