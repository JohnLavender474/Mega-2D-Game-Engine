package com.engine.screens.menus

import com.engine.common.enums.Direction

/** A button that can be selected and navigated. */
interface IMenuButton {

    /**
     * The action that occurs when this button is selected.
     *
     * @param delta the time in seconds since the last frame
     */
    fun onSelect(delta: Float): Boolean

    /**
     * The action that occurs when this button is deselected. Returns the next button.
     *
     * @param direction the direction of the navigation
     * @param delta the time in seconds since the last frame
     * @return the next button
     */
    fun onNavigate(direction: Direction, delta: Float): String?
}
