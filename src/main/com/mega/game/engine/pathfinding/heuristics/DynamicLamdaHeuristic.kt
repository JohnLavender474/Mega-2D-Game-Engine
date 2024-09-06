package com.mega.game.engine.pathfinding.heuristics

/**
 * A heuristic implementation that uses a lambda to dynamically compute the distance between two coordinates.
 *
 * @param distanceLambda A lambda function that takes the start and target coordinates (x1, y1, x2, y2) and returns
 * the calculated distance.
 */
class DynamicLambdaHeuristic(private val distanceLambda: (x1: Int, y1: Int, x2: Int, y2: Int) -> Int) : IHeuristic {

    /**
     * Calculates the distance using the provided lambda function.
     *
     * @param x1 the x coordinate of the first point
     * @param y1 the y coordinate of the first point
     * @param x2 the x coordinate of the second point
     * @param y2 the y coordinate of the second point
     * @return the distance between the two coordinates as calculated by the lambda function
     */
    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int = distanceLambda(x1, y1, x2, y2)
}
