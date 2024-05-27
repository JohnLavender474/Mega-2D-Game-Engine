package com.engine.common.time

/** A runnable that is marked with a time. */
class TimeMarkedRunnable(val time: Float, val runnable: () -> Unit) : Runnable, Comparable<TimeMarkedRunnable> {

    /**
     * Convenience constructor for creating a [TimeMarkedRunnable] with a [Runnable].
     *
     * @param time The time to run the runnable.
     * @param runnable The runnable to run.
     */
    constructor(
        time: Float, runnable: Runnable
    ) : this(time, { runnable.run() })

    override fun run() = runnable()

    override fun compareTo(other: TimeMarkedRunnable) = time.compareTo(other.time)

    override fun equals(other: Any?) = other is TimeMarkedRunnable && other.time.equals(time)

    override fun hashCode() = time.hashCode()
}
