package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.engine.common.objects.IntPair

/**
 * The result data of a pathfinding operation.
 *
 * @param graphPath The path from the start point to the target point, in graph coordinates.
 * @param worldPath The path from the start point to the target point, in world coordinates.
 * @param targetReached True if the start point is equal to the target point, false otherwise. If
 *   this is true, then the paths should be null.
 */
data class PathfinderResult(
    val graphPath: List<IntPair>?,
    val worldPath: List<Vector2>?,
    val targetReached: Boolean
)
