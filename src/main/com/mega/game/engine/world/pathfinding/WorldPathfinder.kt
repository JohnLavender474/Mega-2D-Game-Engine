package com.mega.game.engine.world.pathfinding

import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.pathfinding.IPathfinder
import com.mega.game.engine.pathfinding.Pathfinder
import com.mega.game.engine.pathfinding.Pathfinder.Companion.DEFAULT_MAX_DISTANCE
import com.mega.game.engine.pathfinding.Pathfinder.Companion.DEFAULT_MAX_ITERATIONS
import com.mega.game.engine.pathfinding.Pathfinder.Companion.DEFAULT_RETURN_BEST_PATH_ON_FAILURE
import com.mega.game.engine.pathfinding.PathfinderResult
import com.mega.game.engine.pathfinding.heuristics.IHeuristic
import java.util.function.Predicate

/**
 * A specialized implementation of [IPathfinder] that performs pathfinding based on grid-based coordinates.
 * It uses the [Pathfinder] class to calculate a path from the start to the target position, considering optional
 * filters and world boundaries.
 *
 * The [start] and [target] parameters represent the start and end positions of the path in **grid coordinates**.
 * This class operates entirely in grid space, meaning it directly uses grid units for positioning.
 *
 * The [filter] is an optional parameter that allows users to exclude specific grid cells based on the grid coordinate.
 * This provides flexibility to ignore or include certain areas during the pathfinding process. The [allowDiagonal]
 * parameter controls whether the pathfinding should allow diagonal movement between grid cells, and
 * [allowOutOfWorldBounds] controls whether cells outside the world bounds should be considered traversable.
 *
 * @param start The starting position in grid space.
 * @param target The target position in grid space.
 * @param worldWidth The width of the world in grid units.
 * @param worldHeight The height of the world in grid units.
 * @param allowDiagonal Whether diagonal movement is permitted in the pathfinding process.
 * @param allowOutOfWorldBounds Whether the pathfinding should allow paths outside the defined world boundaries.
 * @param filter Optional [Predicate] to exclude grid cells based on their coordinate. If the predicate returns false,
 * then the coordinate is not considered in the pathfinding. If it returns true, then it will be considered.
 */
class WorldPathfinder(
    private val start: IntPair,
    private val target: IntPair,
    private val worldWidth: Int,
    private val worldHeight: Int,
    private val allowDiagonal: Boolean,
    private val allowOutOfWorldBounds: Boolean,
    private val filter: ((IntPair) -> Boolean)?,
    private val heuristic: IHeuristic,
    private val maxIterations: Int = DEFAULT_MAX_ITERATIONS,
    private val maxDistance: Int = DEFAULT_MAX_DISTANCE,
    private val returnBestPathOnFailure: Boolean = DEFAULT_RETURN_BEST_PATH_ON_FAILURE
) : IPathfinder {

    /**
     * Convenience constructor to allow compatibility with Java's [Predicate] interface for the [filter] parameter,
     * converting it into a Kotlin lambda function.
     *
     * @param start The starting position in grid space.
     * @param target The target position in grid space.
     * @param worldWidth The width of the world in grid units.
     * @param worldHeight The height of the world in grid units.
     * @param allowDiagonal Whether diagonal movement is allowed.
     * @param allowOutOfWorldBounds Whether the pathfinding should allow paths outside the defined world boundaries.
     * @param filter Optional [Predicate] to exclude grid cells based on their coordinate. If the predicate returns
     * false, then the coordinate is not considered in the pathfinding. If it returns true, then it will be considered.
     */

    constructor(
        start: IntPair,
        target: IntPair,
        worldWidth: Int,
        worldHeight: Int,
        allowDiagonal: Boolean,
        allowOutOfWorldBounds: Boolean,
        filter: Predicate<IntPair>?,
        heuristic: IHeuristic,
        maxIterations: Int,
        maxDistance: Int,
        returnBestPathOnFailure: Boolean
    ) : this(
        start,
        target,
        worldWidth,
        worldHeight,
        allowDiagonal,
        allowOutOfWorldBounds,
        filter?.let { { coordinate: IntPair -> it.test(coordinate) } },
        heuristic,
        maxIterations,
        maxDistance,
        returnBestPathOnFailure
    )

    /**
     * Checks if the given [coordinate] is out of the defined world bounds.
     *
     * @param coordinate The grid coordinate to check.
     * @return `true` if the coordinate is out of bounds, `false` otherwise.
     */
    private fun outOfWorldBounds(coordinate: IntPair) =
        coordinate.x < 0 || coordinate.y < 0 || coordinate.x >= worldWidth || coordinate.y >= worldHeight

    /**
     * Executes the pathfinding operation and returns a [PathfinderResult]. The pathfinding checks whether
     * a cell is valid based on the [filter] and the world bounds. If [allowOutOfWorldBounds] is `false`, the method
     * excludes cells outside the bounds of the world from the path.
     *
     * @return A [PathfinderResult] containing the calculated path (if any) and whether a valid path was found.
     */
    override fun call(): PathfinderResult {
        val pathfinder = Pathfinder(
            start,
            target,
            {
                if (!allowOutOfWorldBounds && outOfWorldBounds(it)) false
                else if (filter?.invoke(it) == false) false
                else true
            },
            allowDiagonal,
            heuristic,
            maxIterations,
            maxDistance,
            returnBestPathOnFailure
        )
        return pathfinder.call()
    }
}
