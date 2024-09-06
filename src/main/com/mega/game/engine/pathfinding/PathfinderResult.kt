package com.mega.game.engine.pathfinding

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.Properties

/**
 * The resulting data of a pathfinding operation.
 *
 * @param path The path from the start point to the target point in graph coordinates. If this value is null,
 * then no path was found. The elements of this array should be scaled to world coordinates before being used.
 * @param targetReached True if the start point is equal to the target point, false otherwise.
 * @param properties Optional properties to use for extending the pathfinding results.
 */
data class PathfinderResult(
    val path: Array<IntPair>?,
    val targetReached: Boolean,
    override val properties: Properties = Properties()
) : IPropertizable
