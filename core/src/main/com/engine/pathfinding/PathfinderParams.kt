package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2

/**
 * The parameters used to create a [Pathfinder].
 *
 * @param startSupplier A supplier that supplies the start point.
 * @param targetSupplier A supplier that supplies the target point.
 * @param filter A filter that filters out objects that should not be considered when pathfinding.
 * @param targetListener A listener that is called when the target point changes.
 */
class PathfinderParams(
    val startSupplier: () -> Vector2,
    val targetSupplier: () -> Vector2,
    val filter: (Any) -> Boolean,
    val targetListener: (Vector2) -> Boolean
)
