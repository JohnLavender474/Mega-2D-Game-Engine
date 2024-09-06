package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.abs

/**
 * A heuristic implementation that calculates the Manhattan distance between two points.
 * Manhattan distance, also known as "taxicab" or "grid" distance, is the sum of the absolute differences
 * between the x-coordinates and y-coordinates. It is appropriate for grid-based pathfinding where movement
 * is restricted to horizontal and vertical directions.
 *
 * Formula: abs(x1 - x2) + abs(y1 - y2)
 */
class ManhattanHeuristic : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int = abs(x1 - x2) + abs(y1 - y2)
}
