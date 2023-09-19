package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.Properties

/**
 * An interface for the game engine. The game engine is the main class of the game, and it contains
 * all of the [GameSystem]s and [GameEntity]s.
 */
interface IGameEngine : Updatable, Resettable {

  /** The [GameSystem]s in this [IGameEngine]. */
  val systems: Collection<IGameSystem>

  /**
   * Spawns a [GameEntity] with the given [Properties].
   *
   * @param entity the [GameEntity] to spawn
   * @param spawnProps the [Properties] to spawn the [GameEntity] with
   * @return whether the [GameEntity] was spawned
   */
  fun spawn(entity: GameEntity, spawnProps: Properties): Boolean

  /** Resets all [GameSystem]s in this [IGameEngine]. */
  override fun reset() = systems.forEach { it.reset() }
}
