package com.mega.game.engine.common.interfaces

import com.mega.game.engine.common.enums.Facing

/**
 * An interface for objects that can face left or right.
 *
 * @see Facing
 */
interface IFaceable {

    /** The [Facing] of this [IFaceable]. */
    var facing: Facing

    /**
     * Returns if this [IFaceable] is facing in the given [facing].
     *
     * @param facing the [Facing] to check
     * @return if this [IFaceable] is facing in the given [facing]
     */
    fun isFacing(facing: Facing) = this.facing == facing

    /** Swaps the [Facing] of this [IFaceable]. */
    fun swapFacing() {
        facing = if (facing == Facing.LEFT) Facing.RIGHT else Facing.LEFT
    }
}
