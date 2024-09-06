package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.abs

/**
 * A heuristic implementation that calculates the Chebyshev distance between two points.
 * Chebyshev distance is appropriate in scenarios where diagonal movement, as well as horizontal and vertical
 * movement, costs the same. It returns the maximum of the absolute differences in x-coordinates and y-coordinates.
 *
 * Formula: max(abs(x1 - x2), abs(y1 - y2))
 */
class ChebyshevHeuristic : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int = maxOf(abs(x1 - x2), abs(y1 - y2))
}
