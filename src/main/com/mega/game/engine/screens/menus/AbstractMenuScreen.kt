package com.mega.game.engine.screens.menus

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.common.enums.Direction
import com.mega.game.engine.screens.BaseScreen

/**
 * Abstract class for menu screens. Handles navigation and selection of buttons.
 *
 * @param pauseSupplier a supplier for if the game is paused, used to determine if [render] should do nothing; default
 * value is a lambda that always returns false
 * @param buttons the map of buttons for this menu screen; default value is an empty map
 * @param firstButtonKey the key of the first button to be selected when [show] is called, null by default
 */
abstract class AbstractMenuScreen(
    protected var buttons: ObjectMap<String, IMenuButton> = ObjectMap(),
    protected var pauseSupplier: () -> Boolean = { false },
    protected var firstButtonKey: String? = null
) : BaseScreen() {

    /**
     * The key for the current button. This is used to determine the current button in the [buttons] map. This field is
     * reset to [firstButtonKey] on each call to [show].
     */
    var currentButtonKey: String? = firstButtonKey

    /**
     * Boolean for whether a selection has been made. By default, if this value is true, then the [render] method will
     * do nothing. If [selectionRequested] returns true, then this field is set to the value of [IMenuButton.onSelect]
     * for the current button.
     */
    var selectionMade = false
        protected set

    /**
     * Gets the direction of the navigation. This is called every frame. If no navigation is made, then null should be
     * returned.
     *
     * @return the direction of the navigation, or null
     */
    protected abstract fun getNavigationDirection(): Direction?

    /**
     * Returns if a selection is requested. This is called every frame. If no selection is requested, then false should
     * be returned.
     *
     * @return if a selection is requested
     */
    protected abstract fun selectionRequested(): Boolean

    /**
     * Called on every frame that [getNavigationDirection] returns a non-null value.
     */
    protected open fun onAnyMovement(direction: Direction) {}

    /**
     * Called once when the following is true:
     * - [selectionMade] is false
     * - [selectionRequested] returns true
     * - [IMenuButton.onSelect] for the current button returns true
     */
    protected open fun onAnySelection() {}

    /**
     * Sets the [selectionMade] field to false and the [currentButtonKey] field to the value of [firstButtonKey].
     * Also, if
     */
    override fun show() {
        selectionMade = false
        currentButtonKey = firstButtonKey
    }

    /**
     * Sets the [selectionMade] field to false and the [currentButtonKey] field to the value of [firstButtonKey].
     */
    override fun reset() {
        selectionMade = false
        currentButtonKey = firstButtonKey
    }

    /**
     * If [selectionMade] is true or the return value of [pauseSupplier] is true, then the render method does nothing.
     *
     * Otherwise, for the current button, if there is a return value for [getNavigationDirection], then the value is
     * applied to the current button via the [IMenuButton.onNavigate] method, and the [onAnyMovement] method is called.
     * If [IMenuButton.onNavigate] returns a new button key, then [currentButtonKey] is set to that value, which means
     * that the current button is changed to the button that corresponds to the new button key.
     *
     * Next, if [selectionRequested] returns true, then [selectionMade] is set to the value returned by the current
     * button's [IMenuButton.onSelect] method.
     */
    override fun render(delta: Float) {
        if (selectionMade || pauseSupplier.invoke()) return

        buttons[currentButtonKey]?.let { button ->
            getNavigationDirection()?.let {
                currentButtonKey = button.onNavigate(it, delta)
                onAnyMovement(it)
            }

            if (selectionRequested()) {
                selectionMade = button.onSelect(delta)
                if (selectionMade) onAnySelection()
            }
        }
    }
}
