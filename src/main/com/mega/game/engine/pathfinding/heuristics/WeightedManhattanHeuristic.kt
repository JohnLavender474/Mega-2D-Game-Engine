package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.abs

/**
 * A weighted variant of the Manhattan distance heuristic. It calculates the Manhattan distance
 * and multiplies it by a given weight. This can be useful when the cost of movement is influenced by
 * certain in-game conditions (like terrain types, speed boosts, etc.).
 *
 * Formula: weight * (abs(x1 - x2) + abs(y1 - y2))
 */
class WeightedManhattanHeuristic(private val weight: Float = 1f) : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int = (weight * (abs(x1 - x2) + abs(y1 - y2))).toInt()
}
