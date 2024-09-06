package com.mega.game.engine.pathfinding

import com.badlogic.gdx.utils.OrderedMap

/**
 * A simple implementation of [AbstractPathfindingSystem] that processes pathfinding entries in a straightforward,
 * single-threaded manner. This class iterates through each entry, runs the associated [IPathfinder], and assigns
 * the result directly to the [PathfindingComponent].
 *
 * This class is designed for cases where multi-threading or asynchronous execution is unnecessary, and pathfinding
 * results can be computed immediately within the same update cycle.
 *
 * @param factory the [IPathfinderFactory] responsible for creating a pathfinder instance for each component
 */
class SimplePathfindingSystem(factory: IPathfinderFactory) : AbstractPathfindingSystem(factory) {

    /**
     * Processes each entry in the [OrderedMap] by invoking the pathfinder's [IPathfinder.call] method. The result of
     * the pathfinder is then assigned to the [PathfindingComponent.currentPath] field.
     *
     * @param entries an [OrderedMap] where the key is the [PathfindingComponent] and the value is the corresponding
     * [IPathfinder] to be executed
     */
    override fun handleEntries(entries: OrderedMap<PathfindingComponent, IPathfinder>) {
        entries.forEach {
            val component = it.key
            val pathfinder = it.value
            val result = pathfinder.call()
            component.currentPath = result
        }
    }
}
