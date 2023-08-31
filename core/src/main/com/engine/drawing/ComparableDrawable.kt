package com.engine.drawing

interface ComparableDrawable : Drawable,
    kotlin.Comparable<ComparableDrawable> {
    val priority: Int

    override fun compareTo(other: ComparableDrawable) = priority - other.priority
}