package com.mega.game.engine.cullables

import com.mega.game.engine.common.interfaces.Resettable

/**
 * An object that can be culled.
 *
 * @see Resettable
 */
interface ICullable : Resettable {

    /**
     * Determines if this object should be culled.
     *
     * @return if this object should be culled.
     */
    fun shouldBeCulled(delta: Float): Boolean

    /** Optional reset method. */
    override fun reset() {}
}
