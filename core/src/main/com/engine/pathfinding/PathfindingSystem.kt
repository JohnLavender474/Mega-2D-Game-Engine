package com.engine.pathfinding

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem
import java.util.concurrent.Executors

/**
 * A system that processes [PathfindingComponent]s. This system is responsible for calling the
 * pathfinder and returning the result to the consumer.
 *
 * @param pathfinderFactory The factory that creates the pathfinder for the pathfinding component.
 * @see [IPathfinder]
 */
class PathfindingSystem(private val pathfinderFactory: (PathfindingComponent) -> IPathfinder) :
    GameSystem(PathfindingComponent::class) {

  // pathfinders are run in separate threads
  internal var execServ = Executors.newCachedThreadPool()

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    // collect the pathfinding components and pathfinders
    val pathfindingComponents = ArrayList<PathfindingComponent>(entities.size)
    val pathfinders = ArrayList<IPathfinder>(entities.size)

    entities.forEach { entity ->
      val pathfindingComponent = entity.getComponent(PathfindingComponent::class) ?: return@forEach

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

    // invoke all the pathfinders and comsume the results
    try {
      val futures = execServ.invokeAll(pathfinders)

      for (i in 0 until futures.size) {
        val result = futures[i].get()
        pathfindingComponents[i].consumer(result)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
