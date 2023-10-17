package com.engine.drawables.sorting

import com.engine.drawables.IDrawable

/**
 * An interface for objects that can be drawn. This interface is used to draw objects generically.
 * This interface extends [IComparableDrawable] to allow for sorting by priority.
 *
 * @param T the type of the drawer
 */
interface IComparableDrawable<T> : IDrawable<T>, Comparable<IComparableDrawable<T>> {

  /** The [DrawingPriority] of this [IComparableDrawable]. */
  val priority: DrawingPriority

  /**
   * Compares this [IComparableDrawable] to another [IComparableDrawable] by priority.
   *
   * @param other the other [IComparableDrawable] to compare to
   * @return a negative integer, zero, or a positive integer as this [IComparableDrawable] is less
   *   than, equal to, or greater than the specified [IComparableDrawable].
   */
  override fun compareTo(other: IComparableDrawable<T>) = priority.compareTo(other.priority)
}
