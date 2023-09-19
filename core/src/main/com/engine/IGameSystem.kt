package com.engine

import com.engine.common.interfaces.Activatable
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/** An [IGameSystem] is an [Updatable] that processes [GameEntity]s. */
interface IGameSystem : Activatable, Updatable, Resettable {

  /** Purges all [GameEntity]s from this [IGameSystem]. */
  fun purge()

  /**
   * Returns whether this [IGameSystem] contains the given [GameEntity].
   *
   * @param e the [GameEntity] to check
   * @return whether this [GameSystem] contains the given [GameEntity]
   */
  fun contains(e: GameEntity): Boolean

  /**
   * Removes the given [GameEntity] from this [IGameSystem].
   *
   * @param e the [GameEntity] to remove
   * @return whether the [GameEntity] was removed
   */
  fun remove(e: GameEntity): Boolean

  /**
   * Adds the given [GameEntity] to this [IGameSystem] if it qualifies.
   *
   * @param e the [GameEntity] to add
   * @return whether the [GameEntity] was added
   */
  fun add(e: GameEntity): Boolean

  /** @see addAll */
  fun addAll(vararg entities: GameEntity): Collection<GameEntity>

  /**
   * Adds all the given [GameEntity]s to this [GameSystem] if they qualify.
   *
   * @param entities the [Collection] of [GameEntity]s to add
   * @return the [GameEntity]s that could not be added
   */
  fun addAll(entities: Collection<GameEntity>): Collection<GameEntity>

  /**
   * Returns whether the given [GameEntity] qualifies to be added to this [IGameSystem].
   *
   * @param e the [GameEntity] to check
   * @return whether the [GameEntity] qualifies
   */
  fun qualifies(e: GameEntity): Boolean
}
