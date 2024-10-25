package com.mega.game.engine.pathfinding

import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.Properties

/**
 * The parameters used in a [Pathfinder].
 *
 * @param startCoordinateSupplier A supplier that supplies the starting coordinate.
 * @param targetCoordinateSupplier A supplier that supplies the target coordinate.
 * @param allowDiagonal A supplier that supplies whether diagonal movement is allowed.
 * @param filter A filter that filters whether the coordinate should be allowed in pathfinding. If it returns true, then
 * the coordinate is allowed. If it returns false, then the coordinate is not allowed.
 * @param properties Properties that can optionally be used to extend or modify functionality.
 */
class PathfinderParams(
    var startCoordinateSupplier: () -> IntPair,
    var targetCoordinateSupplier: () -> IntPair,
    var allowDiagonal: () -> Boolean,
    var filter: (IntPair) -> Boolean,
    override val properties: Properties = Properties()
) : IPropertizable
