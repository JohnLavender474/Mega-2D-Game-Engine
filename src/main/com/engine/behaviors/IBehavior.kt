package com.engine.behaviors

import com.engine.common.interfaces.Initializable
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/**
 * An interface that represents a behavior. This interface extends [Initializable], [Updatable], and [Resettable].
 */
interface IBehavior : Initializable, Updatable, Resettable {

    /**
     * Returns whether this [IBehavior] is active.
     *
     * @return Whether this [IBehavior] is active.
     */
    fun isActive(): Boolean

    /**
     * Evaluates this [IBehavior] and returns whether it should be active.
     *
     * @param delta The time in seconds since the last frame.
     * @return Whether this [IBehavior] should be active.
     */
    fun evaluate(delta: Float): Boolean

    /**
     * Performs the action of this [IBehavior]. This method should be called once per frame when the behavior is
     * active.
     *
     * @param delta The time in seconds since the last frame.
     */
    fun act(delta: Float)

    /**
     * Ends this [IBehavior]. This is called when this behavior becomes inactive.
     */
    fun end()
}