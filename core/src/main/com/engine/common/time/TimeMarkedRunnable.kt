package com.engine.common.time

abstract class TimeMarkedRunnable(val time: Float) : Runnable, Comparable<TimeMarkedRunnable> {

  override fun compareTo(other: TimeMarkedRunnable) = time.compareTo(other.time)

  override fun equals(other: Any?) = other is TimeMarkedRunnable && other.time.equals(time)

  override fun hashCode() = time.hashCode()
}
