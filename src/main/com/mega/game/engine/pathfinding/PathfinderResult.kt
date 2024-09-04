package com.mega.game.engine.pathfinding

import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.shapes.GameRectangle

/**
 * The result data of a pathfinding operation. Implements [IPropertizable] in case the user wishes
 * to include extra data in the result.
 *
 * @param graphPath The path from the start point to the target point, in graph coordinates. If this
 *   value is null, then no path was found.
 * @param worldPath The path from the start point to the target point, in world nodes. If this value
 *   is null, then no patch was found.
 * @param targetAlreadyReached True if the start point is equal to the target point and the points
 *   are not null, false otherwise.
 */
data class PathfinderResult(
    val graphPath: ArrayList<IntPair>?,
    val worldPath: ArrayList<GameRectangle>?,
    val targetAlreadyReached: Boolean,
    override val properties: Properties = Properties()
) : IPropertizable
