package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.engine.common.objects.IntPair

/**
 * The parameters used to create a [Pathfinder].
 *
 * @param startSupplier A supplier that supplies the start point.
 * @param targetSupplier A supplier that supplies the target point.
 * @param allowDiagonal A supplier that supplies whether diagonal movement is allowed.
 * @param filter A filter that filters out objects that should not be considered when pathfinding.
 *   If the filter returns false, the node will not be considered when pathfinding.
 */
class PathfinderParams(
    val startSupplier: () -> Vector2,
    val targetSupplier: () -> Vector2,
    val allowDiagonal: (() -> Boolean) = { true },
    val filter: ((IntPair, Collection<Any>) -> Boolean) = { _, _ -> true },
) {

  override fun toString() =
      "PathfinderParams(currentStart=${startSupplier()}, " +
          "currentTarget=${targetSupplier()}, " +
          "currentlyAllowsDiagonal=${allowDiagonal()}"
}
