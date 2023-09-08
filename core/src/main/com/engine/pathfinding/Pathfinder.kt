package com.engine.pathfinding

import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameRectangle
import com.engine.graph.GraphMap
import com.engine.graph.convertToGraphCoordinate
import java.util.*
import java.util.concurrent.Callable
import kotlin.math.abs

/**
 * A pathfinder that finds a path from a start point to a target point. Implements the [Callable]
 * interface so that it can be called in a separate thread if desired. Pathfinding is usually an
 * expensive operation, so it might be good to call this in a separate thread.
 *
 * If a path is found, the [call] function should return a collection of [IntPair]s that represent
 * the path from the start point to the target point. Otherwise, it should return null.
 *
 * @param worldGraph the graph that represents the world
 * @param params the parameters used to create this pathfinder
 * @see [IPathfinder]
 */
class Pathfinder(private val worldGraph: GraphMap, private val params: PathfinderParams) :
    IPathfinder {

  /**
   * A node in the worldGraph. A node is a point in the worldGraph that has a position and a list of
   * edges that connect it to other nodes. The edges are the nodes that are adjacent to this node.
   *
   * @param coordinate the coordinate of this node
   * @param ppm the number of pixels per meter
   */
  internal class Node(val coordinate: IntPair, val ppm: Int) : Comparable<Node> {

    val x: Int
      get() = coordinate.x
    val y: Int
      get() = coordinate.y

    val bounds = GameRectangle(coordinate.x.toFloat() * ppm, coordinate.y.toFloat() * ppm, ppm, ppm)

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

    override fun toString() = "Node(coordinate=$coordinate, bounds=$bounds)"
  }

  /**
   * Finds a path from the start point to the target point. If a path is found, this function should
   * return a collection of [IntPair]s that represent the path from the start point to the target
   * point. Otherwise, it should return null.
   *
   * @return a collection of [IntPair]s that represent the path from the start point to the target
   *   point, or null if no path was found
   */
  override fun call(): Collection<IntPair>? {
    val map = HashMap<IntPair, Node>()

    val targetCoordinate = worldGraph.convertToGraphCoordinate(params.targetSupplier())
    val startCoordinate = worldGraph.convertToGraphCoordinate(params.startSupplier())

    val startNode = Node(startCoordinate, worldGraph.ppm)
    map[startCoordinate] = startNode

    val open = PriorityQueue<Node>()
    open.add(startNode)

    while (open.isNotEmpty()) {
      val currentNode = open.poll()
      currentNode.discovered = true

      if (currentNode.coordinate == targetCoordinate) {
        val path = LinkedList<IntPair>()

        var node = currentNode
        while (node != null) {
          path.addFirst(node.coordinate)
          node = node.previous
        }

        return path
      }

      val currentCoordinate = currentNode.coordinate
      val min = currentCoordinate - 1
      val max = currentCoordinate + 1

      for (x in min.x..max.x) {
        for (y in min.y..max.y) {
          if (isOutOfBounds(x, y)) continue

          if (!params.allowDiagonal && (x == min.x || x == max.x) && (y == min.y || y == max.y))
              continue

          val neighborCoordinate = IntPair(x, y)
          var neighbor = map[neighborCoordinate]

          if (neighbor?.discovered == true) {
            continue
          }

          if (x != targetCoordinate.x &&
              y != targetCoordinate.y &&
              !params.filter(worldGraph.get(x, y))) {
            continue
          }

          val totalDistance = currentNode.distance + cost(currentNode.x, currentNode.y, x, y)

          if (neighbor == null) {
            neighbor = Node(neighborCoordinate, worldGraph.ppm)

            map[neighborCoordinate] = neighbor

            neighbor.distance = totalDistance
            neighbor.previous = currentNode
            open.add(neighbor)
          } else {
            neighbor.distance = totalDistance
            neighbor.previous = currentNode

            open.remove(neighbor)
            open.add(neighbor)
          }
        }
      }
    }

    return null
  }

  internal fun isOutOfBounds(x: Int, y: Int) =
      x < 0 || x >= worldGraph.width || y < 0 || y >= worldGraph.height

  internal fun cost(x1: Int, y1: Int, x2: Int, y2: Int) = abs(x1 - x2) + abs(y1 - y2)
}
