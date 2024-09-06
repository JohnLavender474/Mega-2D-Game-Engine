package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.abs

/**
 * A heuristic implementation that calculates the Octile distance between two points.
 * Octile distance is a variant of the Euclidean distance that is often used for grid-based pathfinding
 * where diagonal movement is allowed but has a different cost than horizontal and vertical movement.
 * This is useful in games where diagonal moves have a slightly higher cost than orthogonal ones.
 *
 * Formula: D * (abs(x1 - x2) + abs(y1 - y2)) + (D2 - 2 * D) * min(abs(x1 - x2), abs(y1 - y2))
 * Where D is the cost for orthogonal movement, and D2 is the cost for diagonal movement.
 */
class OctileHeuristic(
    private val orthogonalCost: Int = 1,
    private val diagonalCost: Int = 1
) : IHeuristic {


    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        val dx = abs(x1 - x2)
        val dy = abs(y1 - y2)
        return orthogonalCost * (dx + dy) + (diagonalCost - 2 * orthogonalCost) * minOf(dx, dy)
    }
}
