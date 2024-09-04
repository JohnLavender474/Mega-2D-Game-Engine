package com.mega.game.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.Properties
import java.util.function.BiPredicate
import java.util.function.Supplier

/**
 * The parameters used to create a [Pathfinder].
 *
 * @param startSupplier A supplier that supplies the start point.
 * @param targetSupplier A supplier that supplies the target point.
 * @param allowDiagonal A supplier that supplies whether diagonal movement is allowed.
 * @param filter A filter that filters out objects that should not be considered when pathfinding.
 *   If the filter returns false, the node will not be considered when pathfinding.
 * @param properties optional props
 */
class PathfinderParams(
    val startSupplier: () -> Vector2,
    val targetSupplier: () -> Vector2,
    val allowDiagonal: (() -> Boolean) = { true },
    val filter: ((IntPair, Iterable<Any>) -> Boolean) = { _, _ -> true },
    override val properties: Properties = Properties()
) : IPropertizable {

    /**
     * Creates a new [PathfinderParams] with the given suppliers and filter.
     *
     * @param startSupplier A supplier that supplies the start point.
     * @param targetSupplier A supplier that supplies the target point.
     * @param allowDiagonal A supplier that supplies whether diagonal movement is allowed.
     * @param filter A filter that filters out objects that should not be considered when pathfinding.
     *  If the filter returns false, the node will not be considered when pathfinding.
     * @param properties optional props
     */
    constructor(
        startSupplier: Supplier<Vector2>,
        targetSupplier: Supplier<Vector2>,
        allowDiagonal: Boolean,
        filter: BiPredicate<IntPair, Iterable<Any>> = BiPredicate { _, _ -> true },
        properties: Properties = Properties()
    ) : this(
        { startSupplier.get() },
        { targetSupplier.get() },
        { allowDiagonal },
        { it1, it2 -> filter.test(it1, it2) },
        properties
    )

    override fun toString() =
        "PathfinderParams(currentStart=${startSupplier()}, " +
                "currentTarget=${targetSupplier()}, " +
                "currentlyAllowsDiagonal=${allowDiagonal()}"
}
