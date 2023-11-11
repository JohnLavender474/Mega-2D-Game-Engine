package com.engine.pathfinding

import com.engine.common.time.Timer
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A component that handles pathfinding.
 *
 * @param entity The entity.
 * @param params The pathfinder params.
 * @param consumer The consumer of the pathfinder result.
 * @param doUpdate The function that determines if the pathfinder should update. By default, the
 *   pathfinder will always update.
 * @property updateIntervalTimer The timer for the interval between pathfinding updates. Default
 *   interval is 0.25 seconds (4 updates per second).
 */
class PathfindingComponent(
    override val entity: IGameEntity,
    var params: PathfinderParams,
    var consumer: (PathfinderResult) -> Unit,
    var doUpdate: () -> Boolean = { true }
) : IGameComponent {

  var updateIntervalTimer: Timer = Timer(0.25f)
}
