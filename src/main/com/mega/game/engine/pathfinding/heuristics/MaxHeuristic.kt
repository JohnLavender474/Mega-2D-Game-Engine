package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.abs

/**
 * A heuristic implementation that calculates the maximum difference between x-coordinates and y-coordinates.
 * Similar to Chebyshev distance, it is ideal when diagonal movement is allowed and has the same cost as horizontal
 * or vertical movement, but it can be used when you're interested in the larger of the two directional differences.
 *
 * Formula: max(abs(x1 - x2), abs(y1 - y2))
 */
class MaxHeuristic : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int = maxOf(abs(x1 - x2), abs(y1 - y2))
}
