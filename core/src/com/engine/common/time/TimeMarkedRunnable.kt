package com.engine.common.time

data class TimeMarkedRunnable(val time: Float, val runnable: Runnable) : Runnable, Comparable<TimeMarkedRunnable> {
    override fun run() = runnable.run()

    override fun compareTo(other: TimeMarkedRunnable) = time.compareTo(other.time)

    override fun equals(other: Any?) = other is TimeMarkedRunnable && other.time.equals(time)

    override fun hashCode() = time.hashCode()
}
