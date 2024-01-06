package com.engine.common.interfaces

import com.engine.common.enums.Facing

/**
 * An interface for objects that can face left or right.
 *
 * @see Facing
 */
interface IFaceable {

  /** The [Facing] of this [IFaceable]. */
  var facing: Facing
}

/**
 * Returns if this [IFaceable] is facing in the given [facing].
 *
 * @param facing the [Facing] to check
 * @return if this [IFaceable] is facing in the given [facing]
 */
fun IFaceable.isFacing(facing: Facing) = this.facing == facing

/** Swaps the [Facing] of this [IFaceable]. */
fun IFaceable.swapFacing() {
  facing = if (facing == Facing.LEFT) Facing.RIGHT else Facing.LEFT
}
