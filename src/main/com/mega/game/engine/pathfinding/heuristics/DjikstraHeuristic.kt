package com.mega.game.engine.pathfinding.heuristics

/**
 * A heuristic implementation that returns zero for every pair of points. This effectively turns the pathfinding
 * algorithm into Dijkstra's Algorithm, where the shortest path is found based on actual distance traveled without
 * any heuristic guidance.
 *
 * This is useful when you want the pathfinding to strictly consider the actual movement costs, without any estimation
 * of the shortest path.
 */
class DijkstraHeuristic : IHeuristic {

    override fun calculate(x1: Int, y1: Int, x2: Int, y2: Int): Int = 0
}
