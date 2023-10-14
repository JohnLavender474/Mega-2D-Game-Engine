package com.engine.drawables

/**
 * An interface for objects that can be drawn. This interface is used to draw objects generically.
 * This interface extends [IComparableDrawable] to allow for sorting by priority.
 *
 * @param T the type of the drawer
 */
interface IComparableDrawable<T> : IDrawable<T>, Comparable<IComparableDrawable<T>> {
  val priority: DrawingPriority

  override fun compareTo(other: IComparableDrawable<T>) = priority.compareTo(other.priority)
}

/**
 * A [DrawingSection] is used to sort [IDrawable]s by section.
 *
 * @see DrawingPriority
 */
enum class DrawingSection {
  BACKGROUND,
  PLAYGROUND,
  FOREGROUND
}

/**
 * A [DrawingPriority] is used to sort [IDrawable]s by priority.
 *
 * @param section the [DrawingSection] of the [IDrawable]
 * @param priority the priority of the [IDrawable]
 * @see DrawingSection
 */
data class DrawingPriority(var section: DrawingSection, var priority: Int) :
    Comparable<DrawingPriority> {

  override fun compareTo(other: DrawingPriority): Int {
    val sectionCompare = section.compareTo(other.section)
    return if (sectionCompare == 0) {
      priority.compareTo(other.priority)
    } else {
      sectionCompare
    }
  }
}
