package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.Properties
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
import com.engine.systems.IGameSystem

/**
 * An interface for the game engine. The game engine is the main class of the game, and it contains
 * all of the [IGameSystem]s and [GameEntity]s.
 */
interface IGameEngine : Updatable, Resettable {

    /** The [IGameSystem]s in this [IGameEngine]. */
    val systems: Iterable<IGameSystem>

    /**
     * Spawns a [GameEntity] with the given [Properties].
     *
     * @param entity the [GameEntity] to spawn
     * @param spawnProps the [Properties] to spawn the [GameEntity] with
     * @return whether the [GameEntity] was spawned
     */
    fun spawn(entity: IGameEntity, spawnProps: Properties): Boolean
}
