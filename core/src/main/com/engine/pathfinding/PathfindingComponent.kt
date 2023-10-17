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
 * @param updateIntervalTimer The timer for the interval between pathfinding updates. Default
 *   interval is 0.25 seconds (4 updates per second).
 */
class PathfindingComponent(
    override val entity: IGameEntity,
    var params: PathfinderParams,
    var consumer: (PathfinderResult) -> Unit,
    var updateIntervalTimer: Timer = Timer(0.25f)
) : IGameComponent
