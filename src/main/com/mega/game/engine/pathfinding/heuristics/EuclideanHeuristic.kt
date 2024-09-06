package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.sqrt

/**
 * A heuristic implementation that calculates the Euclidean distance between two points.
 * Euclidean distance is the straight-line distance between two points, and is commonly used
 * in pathfinding scenarios where diagonal movement is allowed, and the shortest path is desired.
 *
 * Formula: sqrt((x1 - x2)^2 + (y1 - y2)^2)
 */
class EuclideanHeuristic : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int =
        sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toFloat()).toInt()
}
