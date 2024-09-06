package com.mega.game.engine.pathfinding

import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/**
 * An abstract pathfinding system responsible for managing the process of pathfinding. The system iterates over all
 * entities and checks for each [PathfindingComponent] if a pathfinder should be invoked. If so, the system creates
 * a new [IPathfinder] instance using the provided [IPathfinderFactory].
 *
 * The method [process] is responsible for collecting the pathfinding components that need to be processed and their
 * corresponding pathfinders. The entries of pathfinding components and pathfinders are passed to the [handleEntries]
 * method as two separate collections. The pathfinder at index i in the collection corresponds to the component at
 * index i in the other collection.
 *
 * This class provides a framework for implementing systems that perform pathfinding, leaving the actual handling of
 * the pathfinding logic (such as threading or result processing) to subclasses.
 *
 * @param factory the [IPathfinderFactory] responsible for creating a pathfinder instance for each component
 */
abstract class AbstractPathfindingSystem(private val factory: IPathfinderFactory) :
    GameSystem(PathfindingComponent::class) {

    companion object {
        const val TAG = "AbstractPathfindingSystem"
    }

    /**
     * Handles the components and pathfinders that need to be processed. This method is abstract and must be implemented
     * by subclasses to define how the system processes the given components and pathfinders. The pathfinder at index i
     * in the pathfinders collection corresponds to the component at index i in the components collection.
     *
     * @param entries an [OrderedMap] where the key is the [PathfindingComponent] and the value is the corresponding
     * [IPathfinder] to be executed
     */
    protected abstract fun handleEntries(entries: OrderedMap<PathfindingComponent, IPathfinder>)

    /**
     * Processes the [PathfindingComponent]s for each entity in the provided [entities] collection. The system checks
     * whether the pathfinding component should be updated based on its timer and other conditions.
     *
     * If the component should be updated, a new pathfinder is created using the provided [IPathfinderFactory]. The
     * components and their corresponding pathfinders are collected in an [OrderedMap], which is passed to the abstract
     * [handleEntries] method for further processing.
     *
     * @param on a boolean flag indicating whether the system is active and should process entities
     * @param entities a collection of entities to process, each of which may have a [PathfindingComponent]
     * @param delta the time in seconds since the last update
     */
    final override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return
        val entries = OrderedMap<PathfindingComponent, IPathfinder>()
        entities.forEach { entity ->
            val component = entity.getComponent(PathfindingComponent::class) ?: return@forEach

            // Consume the current path
            val currentPath = component.currentPath
            if (currentPath != null) component.consumer(currentPath)

            // Update the interval timer
            val updateIntervalTimer = component.intervalTimer
            updateIntervalTimer.update(delta)
            if (!updateIntervalTimer.isFinished()) return@forEach

            // Reset the update interval timer
            updateIntervalTimer.reset()

            // Check if the component should update
            if (!component.doUpdate()) return@forEach

            // Create the pathfinder and add it to the entries map
            val pathfinder = factory.getPathfinder(component.params)
            entries.put(component, pathfinder)
        }
        handleEntries(entries)
    }
}
