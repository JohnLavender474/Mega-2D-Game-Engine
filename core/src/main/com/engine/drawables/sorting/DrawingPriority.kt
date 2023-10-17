package com.engine.drawables.sorting

import com.engine.drawables.IDrawable

/**
 * A [DrawingPriority] is used to sort [IDrawable]s by priority.
 *
 * @param section the [DrawingSection] of the [IDrawable]
 * @param priority the priority of the [IDrawable]
 * @see DrawingSection
 */
data class DrawingPriority(var section: DrawingSection, var priority: Int) :
    Comparable<DrawingPriority> {

  /**
   * Compares this [DrawingPriority] to another [DrawingPriority] by section and priority.
   *
   * @param other the other [DrawingPriority] to compare to
   * @return a negative integer, zero, or a positive integer as this [DrawingPriority] is less than,
   *   equal to, or greater than the specified [DrawingPriority].
   */
  override fun compareTo(other: DrawingPriority): Int {
    val sectionCompare = section.compareTo(other.section)
    return if (sectionCompare == 0) {
      priority.compareTo(other.priority)
    } else {
      sectionCompare
    }
  }
}
