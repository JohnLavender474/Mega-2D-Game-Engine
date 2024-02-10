package com.engine.common.interfaces

/**
 * A function that can be used to update an object.
 *
 * @param T The type of object to update.
 */
fun interface UpdateFunction<T> {

    /**
     * Updates the given object.
     *
     * @param delta The time in seconds since the last update.
     * @param t The object to update.
     */
    fun update(delta: Float, t: T)
}
