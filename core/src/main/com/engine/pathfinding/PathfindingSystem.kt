package com.engine.pathfinding

import com.engine.common.GameLogger
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A system that processes [PathfindingComponent]s. This system is responsible for calling the
 * pathfinder and returning the result to the consumer.
 *
 * @param pathfinderFactory The factory that creates the pathfinder for the pathfinding component.
 * @param timeout The timeout for the pathfinder. If the pathfinder takes longer than this timeout,
 *   then the pathfinder will be cancelled. If this is null, then the pathfinder will not time out.
 *   Default is null.
 * @param timeoutUnit The unit of the timeout. Default is null. If this is null, then the pathfinder
 *   will not time out.
 * @see [IPathfinder]
 */
class PathfindingSystem(
    private val pathfinderFactory: (PathfindingComponent) -> IPathfinder,
    var timeout: Long? = null,
    var timeoutUnit: TimeUnit? = null
) : GameSystem(PathfindingComponent::class) {

    companion object {
        const val TAG = "PathfindingSystem"
    }

    // pathfinders are run in separate threads
    internal var execServ = Executors.newCachedThreadPool()

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        // collect the pathfinding components and pathfinders
        val pathfindingComponents = ArrayList<PathfindingComponent>(entities.size)
        val pathfinders = ArrayList<IPathfinder>(entities.size)

        entities.forEach { entity ->
            val pathfindingComponent = entity.getComponent(PathfindingComponent::class) ?: return@forEach

            // consume the current path
            val currentPath = pathfindingComponent.currentPath
            if (currentPath != null) pathfindingComponent.consumer(currentPath)

            // update the update interval timer
            // if the timer is not finished, then do not run a pathfinder for this pathfinding component
            val updateIntervalTimer = pathfindingComponent.updateIntervalTimer
            updateIntervalTimer.update(delta)
            if (!updateIntervalTimer.isFinished()) return@forEach

            // reset the update interval timer
            updateIntervalTimer.reset()

            // if the pathfinding component does not want to update, then do not run a pathfinder for
            // this pathfinding component
            if (!pathfindingComponent.doUpdate()) return@forEach

            // submit the pathfinding component and pathfinder entry
            val pathfinder = pathfinderFactory(pathfindingComponent)
            pathfindingComponents.add(pathfindingComponent)
            pathfinders.add(pathfinder)
        }

        // invoke all the pathfinders and set the results to the respective [currentPath] field
        try {
            val futures = execServ.invokeAll(pathfinders)

            for (i in 0 until futures.size) {
                try {
                    val result =
                        if (timeout == null || timeoutUnit == null) futures[i].get()
                        else futures[i].get(timeout!!, timeoutUnit!!)
                    pathfindingComponents[i].currentPath = result
                } catch (e: Exception) {
                    e.printStackTrace()
                    GameLogger.error(TAG, "Error occurred while invoking pathfinding future for index $i: $e")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GameLogger.debug(TAG, "Error occurred while invoking pathfinding futures list: $e")
        }
    }
}
