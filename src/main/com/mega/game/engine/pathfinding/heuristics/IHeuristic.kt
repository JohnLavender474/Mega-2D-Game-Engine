package com.mega.game.engine.pathfinding.heuristics

/**
 * Interface to define a heuristic for calculating the cost between two points in the pathfinding algorithm.
 */
interface IHeuristic {

    /**
     * Calculates the cost between two points, (x1, y1) and (x2, y2).
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the heuristic cost between the two points
     */
    fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int
}
