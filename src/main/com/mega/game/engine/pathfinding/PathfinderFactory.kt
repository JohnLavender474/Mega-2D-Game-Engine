package com.mega.game.engine.pathfinding

/**
 * Functional interface for creating an [IPathfinder] with the given [PathfinderParams].
 */
interface IPathfinderFactory {

    /**
     * Returns an [IPathfinder] with the given [params].
     *
     * @param params the pathfinder params
     * @return the [IPathfinder]
     */
    fun getPathfinder(params: PathfinderParams): IPathfinder
}