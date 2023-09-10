package com.engine.pathfinding

import com.engine.common.objects.IntPair
import com.engine.common.objects.pairTo
import com.engine.graph.GraphMap
import com.engine.graph.convertToGraphCoordinate
import com.engine.graph.convertToWorldCoordinate
import com.engine.graph.isOutOfBounds
import java.util.*
import java.util.concurrent.Callable
import kotlin.math.abs

/**
 * A pathfinder that finds a graphPath from a start point to a target point. Implements the
 * [Callable] interface so that it can be called in a separate thread if desired. Pathfinding is
 * usually an expensive operation, so it might be good to call this in a separate thread.
 *
 * If a graphPath is found, the [call] function should return a collection of [IntPair]s that
 * represent the graphPath from the start point to the target point. Otherwise, it should return
 * null.
 *
 * @param graph the graph that represents the world
 * @param params the parameters used to create this pathfinder
 * @see [IPathfinder]
 */
class Pathfinder(private val graph: GraphMap, private val params: PathfinderParams) : IPathfinder {

  /**
   * A node in the graph. A node is a point in the graph that has a position and a list of edges
   * that connect it to other nodes. The edges are the nodes that are adjacent to this node.
   *
   * @param coordinate the coordinate of this node
   * @param ppm the number of pixels per meter
   */
  internal class Node(val coordinate: IntPair, val ppm: Int) : Comparable<Node> {

    val x: Int
      get() = coordinate.first
    val y: Int
      get() = coordinate.second

    var distance = 0
    var previous: Node? = null
    var discovered = false

    override fun compareTo(other: Node) = distance.compareTo(other.distance)

    override fun hashCode(): Int {
      var hash = 49
      hash = hash * 31 + coordinate.first
      hash = hash * 31 + coordinate.second
      return hash
    }

    override fun equals(other: Any?) = other is Node && other.coordinate == coordinate
  }

  /**
   * Finds a graphPath from the start point to the target point. If a graphPath is found, this
   * function should return a collection of [IntPair]s that represent the graphPath from the start
   * point to the target point. Otherwise, it should return null.
   *
   * @return a collection of [IntPair]s that represent the graphPath from the start point to the
   *   target point, or null if no graphPath was found
   */
  override fun call(): PathfinderResult {
    // A map that maps coordinates to nodes
    val map = HashMap<IntPair, Node>()

    // Convert the start and target points to graph coordinates
    val targetCoordinate = graph.convertToGraphCoordinate(params.targetSupplier())
    val startCoordinate = graph.convertToGraphCoordinate(params.startSupplier())

    // If the start or target points are out of bounds, return null
    if (graph.isOutOfBounds(targetCoordinate) || graph.isOutOfBounds(startCoordinate)) {
      return PathfinderResult(null, null, false)
    }

    // If the start and target points are the same, return an empty graphPath
    if (startCoordinate == targetCoordinate) {
      return PathfinderResult(null, null, true)
    }

    // Add the start node to the map
    val startNode = Node(startCoordinate, graph.ppm)
    map[startCoordinate] = startNode

    // When a node is added, it is sorted based on its distance from all others
    val open = PriorityQueue<Node>()
    open.add(startNode)

    // While there are still nodes to visit
    while (open.isNotEmpty()) {
      // Get the node with the smallest distance
      val currentNode = open.poll()
      currentNode.discovered = true

      // If the current node is the target node, return the result
      if (currentNode.coordinate == targetCoordinate) {
        val graphPath = LinkedList<IntPair>()

        var node = currentNode
        while (node != null) {
          graphPath.addFirst(node.coordinate)
          node = node.previous
        }

        // Convert the graphPath to world coordinates
        val worldPath = graphPath.map { graph.convertToWorldCoordinate(it) }
        return PathfinderResult(graphPath, worldPath, false)
      }

      // Get the coordinates of the current node
      val currentCoordinate = currentNode.coordinate
      // println("Current coordinate: $currentCoordinate")

      val min = currentCoordinate - 1
      val max = currentCoordinate + 1
      // println("Min: $min")
      // println("Max: $max")

      // For each adjacent node
      for (x in min.first..max.first) {
        for (y in min.second..max.second) {
          // If the adjacent node is out of bounds, skip it
          if (graph.isOutOfBounds(x, y)) continue

          // If diagonal movement is not allowed and the adjacent node is diagonal to the current
          // node, skip it
          if (!params.allowDiagonal() &&
              (x == min.first || x == max.first) &&
              (y == min.second || y == max.second))
              continue

          // Get the adjacent node
          val neighborCoordinate = IntPair(x, y)
          var neighbor = map[neighborCoordinate]

          // If the adjacent node has already been discovered, skip it
          if (neighbor?.discovered == true) continue

          // If the adjacent node is an obstacle, skip it
          if (x pairTo y != targetCoordinate && !params.filter(x pairTo y, graph.get(x, y)))
              continue

          // Calculate the total distance from the start node to the adjacent node
          val totalDistance = currentNode.distance + cost(currentNode.x, currentNode.y, x, y)

          // If the adjacent node has not been discovered or the total distance from the start node
          // to the adjacent node is less than the adjacent node's distance from the start node,
          // update the adjacent node's distance from the start node and previous node
          // accordingly
          if (neighbor == null) {
            neighbor = Node(neighborCoordinate, graph.ppm)
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
    }

    return PathfinderResult(null, null, false)
  }

  internal fun cost(x1: Int, y1: Int, x2: Int, y2: Int) = abs(x1 - x2) + abs(y1 - y2)
}
