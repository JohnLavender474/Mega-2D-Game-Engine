package com.engine.common.interfaces

/**
 * A consumer that consumes updates.
 */
interface UpdateConsumer<T> {

    /**
     * Consumes an update.
     *
     * @param delta The time since the last update.
     * @param value The value to consume.
     */
    fun consumeUpdate(delta: Float, value: T)
}