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

/** Swaps the [Facing] of this [IFaceable]. */
fun IFaceable.swapFacing() {
  facing = if (facing == Facing.LEFT) Facing.RIGHT else Facing.LEFT
}
