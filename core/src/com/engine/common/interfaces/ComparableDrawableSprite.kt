package com.engine.common.interfaces

interface ComparableDrawableSprite : DrawableSprite, Comparable<ComparableDrawableSprite> {
    fun getPriority(): Int

    override fun compareTo(other: ComparableDrawableSprite) = getPriority() - other.getPriority()
}