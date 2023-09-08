package com.engine.pathfinding

import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameRectangle
import com.engine.graph.GraphMap
import com.engine.graph.convertToGraphCoordinate

class Pathfinder(private val worldGraph: GraphMap, private val params: PathfinderParams) :
    IPathfinder {

  /**
   * A node in the worldGraph. A node is a point in the worldGraph that has a position and a list of
   * edges that connect it to other nodes. The edges are the nodes that are adjacent to this node.
   *
   * @param x the x coordinate of the node
   * @param y the y coordinate of the node
   * @param ppm the number of pixels per meter
   */
  internal class Node(val x: Int, val y: Int, val ppm: Int) : Comparable<Node> {

    val bounds = GameRectangle(x.toFloat() * ppm, y.toFloat() * ppm, ppm, ppm)

    var distance = 0
    var previous: Node? = null
    var discovered = false

    override fun compareTo(other: Node) = distance.compareTo(other.distance)

    override fun hashCode(): Int {
      var hash = 49
      hash = hash * 31 + x
      hash = hash * 31 + y
      return hash
    }

    override fun equals(other: Any?) = other is Node && other.x == x && other.y == y

    override fun toString() = "Node(x=$x, y=$y, bounds=$bounds)"
  }

  private val map = HashMap<IntPair, Node>()

  override fun call(): Collection<GameRectangle>? {
    val targetCoordinates = worldGraph.convertToGraphCoordinate(params.targetSupplier())
    val startCoordinates = worldGraph.convertToGraphCoordinate(params.startSupplier())

    // TODO: 2021-03-07 add a check to see if the target is in the same cell as the start

    return null
  }
}
