package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.engine.common.interfaces.IPropertizable
import com.engine.common.objects.IntPair
import com.engine.common.objects.Properties

/**
 * The result data of a pathfinding operation. Implements [IPropertizable] in case the user wishes
 * to include extra data in the result.
 *
 * @param graphPath The path from the start point to the target point, in graph coordinates. If this
 *   value is null, then no path was found.
 * @param worldPath The path from the start point to the target point, in world coordinates. If this
 *   value is null, then no patch was found.
 * @param targetReached True if the start point is equal to the target point and the points are not
 *   null, false otherwise.
 */
data class PathfinderResult(
    val graphPath: Array<IntPair>?,
    val worldPath: Array<Vector2>?,
    val targetReached: Boolean,
    override val properties: Properties = Properties()
) : IPropertizable
