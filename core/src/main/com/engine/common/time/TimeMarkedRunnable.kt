package com.engine.common.time

/** A runnable that is marked with a time. */
class TimeMarkedRunnable(val time: Float, val runnable: () -> Unit) :
    Runnable, Comparable<TimeMarkedRunnable> {

  override fun run() = runnable()

  override fun compareTo(other: TimeMarkedRunnable) = time.compareTo(other.time)

  override fun equals(other: Any?) = other is TimeMarkedRunnable && other.time.equals(time)

  override fun hashCode() = time.hashCode()
}
