package com.engine.drawables.sorting

import com.engine.drawables.IDrawable

/**
 * A [DrawingPriority] is used to sort [IDrawable]s by priority.
 *
 * @param section the [DrawingSection] of the [IDrawable]
 * @param value the priority of the [IDrawable]
 * @see DrawingSection
 */
data class DrawingPriority(var section: DrawingSection, var value: Int) :
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
        return if (sectionCompare == 0) value.compareTo(other.value) else sectionCompare
    }

    override fun equals(other: Any?) =
        other is DrawingPriority && section == other.section && value == other.value

    override fun hashCode() = 31 * section.hashCode() + value
}
