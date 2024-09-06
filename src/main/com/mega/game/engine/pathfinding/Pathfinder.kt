package com.mega.game.engine.pathfinding

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.extensions.gdxArrayOf
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.pathfinding.heuristics.IHeuristic
import java.util.*

/**
 * A pathfinder that finds a path from a start point to a target point.
 *
 * If a path is found, the [call] function returns a collection of [IntPair]s that represent the path from the start
 * point to the target point. Otherwise, it returns null.
 *
 * @param startCoordinate A function to get the start point in graph coordinates.
 * @param targetCoordinate A function to get the target point in graph coordinates.
 * @param filter A function to check if the given coordinate is allowed. If true, then the coordiante is allowed. If
 * false, then the coordinate is not allowed.
 * @param allowDiagonal A flag to allow diagonal movement.
 * @param heuristic The heuristic to use for calculating the cost between two coordinates.
 * @param maxIterations The maximum iterations to run before returning if the target is not reached within that amount
 * of iterations.
 * @param maxDistance The maximum distance that can be traveled before the pathfinder returns. If the distance between
 * the start and target exceeds this value, then the pathfinder returns. If within iterations the distance becomes
 * greater than this value, the pathfinder returns early.
 * @param returnBestPathOnFailure If the pathfinder quits early, then it will either return a null path or else the best
 * found path based on this value.
 */
class Pathfinder(
    private val startCoordinate: IntPair,
    private val targetCoordinate: IntPair,
    private val filter: (IntPair) -> Boolean,
    private val allowDiagonal: Boolean,
    private val heuristic: IHeuristic,
    private val maxIterations: Int = DEFAULT_MAX_ITERATIONS,
    private val maxDistance: Int = DEFAULT_MAX_DISTANCE,
    private val returnBestPathOnFailure: Boolean = DEFAULT_RETURN_BEST_PATH_ON_FAILURE
) : IPathfinder {

    companion object {
        const val TAG = "Pathfinder"
        const val DEFAULT_MAX_ITERATIONS = 1000
        const val DEFAULT_MAX_DISTANCE = Integer.MAX_VALUE
        const val DEFAULT_RETURN_BEST_PATH_ON_FAILURE = false
    }

    internal class Node(val coordinate: IntPair) : Comparable<Node> {

        val x: Int
            get() = coordinate.x
        val y: Int
            get() = coordinate.y
        var distance = 0
        var previous: Node? = null
        var discovered = false

        override fun compareTo(other: Node) = distance.compareTo(other.distance)

        override fun hashCode(): Int {
            var hash = 49
            hash = hash * 31 + coordinate.x
            hash = hash * 31 + coordinate.y
            return hash
        }

        override fun equals(other: Any?) = other is Node && other.coordinate == coordinate

        override fun toString() =
            "Node{x=$x,y=$y,distance=$distance,discovered=$discovered,previous={${previous?.let { "${it.x},${it.y}" }}}"
    }

    override fun call(): PathfinderResult {
        val map = HashMap<IntPair, Node>()

        if (startCoordinate == targetCoordinate ||
            (!returnBestPathOnFailure && startCoordinate.toVector2().dst(targetCoordinate.toVector2()) > maxDistance)
        ) return PathfinderResult(null, true)

        val startNode = Node(startCoordinate)
        map[startCoordinate] = startNode

        val open = PriorityQueue<Node>()
        open.add(startNode)

        var iterations = 0
        var bestNode: Node? = null

        while (open.isNotEmpty()) {
            if (iterations >= maxIterations) break
            iterations++

            val currentNode = open.poll()
            currentNode.discovered = true

            if (currentNode.distance > maxDistance) break

            if (bestNode == null || heuristic.calculate(
                    currentNode.x,
                    currentNode.y,
                    targetCoordinate.x,
                    targetCoordinate.y
                ) < heuristic.calculate(bestNode.x, bestNode.y, targetCoordinate.x, targetCoordinate.y)
            ) bestNode = currentNode

            if (currentNode.coordinate == targetCoordinate) {
                val path = buildPath(currentNode)
                return PathfinderResult(path, false)
            }

            val neighborCoordinates = getNeighborCoordinates(currentNode.coordinate)
            neighborCoordinates.forEach { neighborCoordinate ->
                if (!filter.invoke(neighborCoordinate)) return@forEach

                var neighbor = map[neighborCoordinate]
                if (neighbor?.discovered == true) return@forEach

                val totalDistance = currentNode.distance + heuristic.calculate(
                    currentNode.x,
                    currentNode.y,
                    neighborCoordinate.x,
                    neighborCoordinate.y
                )

                // If the adjacent node has not been discovered or the total distance from the start node to the
                // adjacent node is less than the adjacent node's distance from the start node, update the adjacent
                // node's distance from the start node and previous node accordingly
                if (neighbor == null) {
                    neighbor = Node(neighborCoordinate)
                    map[neighborCoordinate] = neighbor

                    neighbor.distance = totalDistance
                    neighbor.previous = currentNode

                    open.add(neighbor)
                } else if (totalDistance < neighbor.distance) {
                    neighbor.distance = totalDistance
                    neighbor.previous = currentNode

                    open.remove(neighbor)
                    open.add(neighbor)
                }
            }
        }

        return if (returnBestPathOnFailure && bestNode != null) {
            val bestPath = buildPath(bestNode)
            PathfinderResult(bestPath, false)
        } else PathfinderResult(null, false)
    }

    private fun buildPath(node: Node): Array<IntPair> {
        val path = Array<IntPair>()
        var currentNode: Node? = node
        while (currentNode != null) {
            path.add(currentNode.coordinate)
            currentNode = currentNode.previous
        }
        path.reverse()
        return path
    }

    private fun getNeighborCoordinates(coordinate: IntPair): Array<IntPair> {
        val (x, y) = coordinate
        return if (allowDiagonal) {
            gdxArrayOf(
                x - 1 pairTo y,
                x + 1 pairTo y,
                x pairTo y - 1,
                x pairTo y + 1,
                x - 1 pairTo y - 1,
                x + 1 pairTo y + 1,
                x - 1 pairTo y + 1,
                x + 1 pairTo y - 1
            )
        } else {
            gdxArrayOf(
                x - 1 pairTo y, x + 1 pairTo y, x pairTo y - 1, x pairTo y + 1
            )
        }
    }
}
