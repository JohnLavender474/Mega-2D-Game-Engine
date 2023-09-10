package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.engine.common.objects.IntPair

/**
 * The result of a pathfinding operation.
 *
 * @param graphPath The path from the start point to the target point, in graph coordinates.
 * @param worldPath The path from the start point to the target point, in world coordinates.
 * @param targetReached True if the target point was reached, false otherwise.
 */
data class PathfinderResult(
    val graphPath: List<IntPair>?,
    val worldPath: List<Vector2>?,
    val targetReached: Boolean
)
