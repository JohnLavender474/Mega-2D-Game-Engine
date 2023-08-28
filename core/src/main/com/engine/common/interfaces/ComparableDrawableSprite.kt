package com.engine.common.interfaces

interface ComparableDrawableSprite : DrawableSprite, Comparable<ComparableDrawableSprite> {
    val priority: Int

    override fun compareTo(other: ComparableDrawableSprite) = priority - other.priority
}