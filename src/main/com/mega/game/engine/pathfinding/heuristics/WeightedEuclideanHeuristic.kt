package com.mega.game.engine.pathfinding.heuristics

import kotlin.math.sqrt

/**
 * A weighted variant of the Euclidean distance heuristic. It calculates the Euclidean distance
 * and multiplies it by a given weight. This is useful when the cost of diagonal or straight-line
 * movement is adjusted by environmental factors, such as terrain.
 *
 * Formula: weight * sqrt((x1 - x2)^2 + (y1 - y2)^2)
 */
class WeightedEuclideanHeuristic(private val weight: Float = 1f) : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int =
        (weight * sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toFloat())).toInt()
}
