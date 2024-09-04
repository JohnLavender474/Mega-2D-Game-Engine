package com.mega.game.engine.drawables.sorting

import com.mega.game.engine.drawables.IDrawable

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