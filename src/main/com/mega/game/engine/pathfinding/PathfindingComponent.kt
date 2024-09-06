package com.mega.game.engine.pathfinding

import com.mega.game.engine.common.time.Timer
import com.mega.game.engine.components.IGameComponent
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * A component that handles pathfinding.
 *
 * @param params The pathfinder params.
 * @param consumer The consumer of the pathfinder result.
 * @param doUpdate Determines if the pathfinder should update. By default, the pathfinder will always update.
 * @property currentPath The current path from the last pathfinder update.
 * @property intervalTimer The timer for the interval between pathfinding updates. Default interval is 0.1 seconds
 * (10 updates per second).
 */
class PathfindingComponent(
    var params: PathfinderParams,
    var consumer: (PathfinderResult) -> Unit,
    var doUpdate: () -> Boolean = { true },
    var intervalTimer: Timer = Timer(DEFAULT_UPDATE_INTERVAL)
) : IGameComponent {

    companion object {
        const val DEFAULT_UPDATE_INTERVAL = 0.1f
    }

    /**
     * Creates a new pathfinding component.
     *
     * @param params The pathfinder params.
     * @param consumer The consumer of the pathfinder result.
     * @param doUpdate The function that determines if the pathfinder should update. By default, the
     */
    constructor(
        params: PathfinderParams, consumer: Consumer<PathfinderResult>, doUpdate: Supplier<Boolean> = Supplier { true }
    ) : this(params, { consumer.accept(it) }, { doUpdate.get() })

    internal var currentPath: PathfinderResult? = null
}
