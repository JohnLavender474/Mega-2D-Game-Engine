package com.engine.screens.menus

import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.enums.Direction
import java.util.function.Supplier

/**
 * Abstract class for menu screens. Handles navigation and selection of buttons. The first button is
 * selected by default.
 *
 * @param pauseSupplier a supplier for if the game is paused
 * @param firstButtonKey the key of the first button to be selected
 */
abstract class MenuScreen(
    protected val pauseSupplier: () -> Boolean,
    protected val firstButtonKey: String,
    protected val buttons: ObjectMap<String, IMenuButton> = ObjectMap()
) : Screen {

    var currentButtonKey: String? = firstButtonKey
    var selectionMade = false
        private set

    /**
     * Constructor for menu screens.
     *
     * @param pauseSupplier a supplier for if the game is paused
     * @param firstButtonKey the key of the first button to be selected
     * @param buttons the map of buttons
     */
    constructor(
        pauseSupplier: Supplier<Boolean>,
        firstButtonKey: String,
        buttons: ObjectMap<String, IMenuButton> = ObjectMap()
    ) : this(
        { pauseSupplier.get() },
        firstButtonKey,
        buttons
    )

    /**
     * Gets the direction of the navigation. This is called every frame. If no navigation is made,
     * then null should be returned.
     *
     * @return the direction of the navigation, or null
     */
    protected abstract fun getNavigationDirection(): Direction?

    /**
     * Returns if a selection is requested. This is called every frame. If no selection is requested,
     * then false should be returned.
     *
     * @return if a selection is requested
     */
    protected abstract fun selectionRequested(): Boolean

    /**
     * Called every frame if any navigation is made.
     *
     * @see getNavigationDirection
     */
    protected open fun onAnyMovement() {}

    override fun show() {
        selectionMade = false
        currentButtonKey = firstButtonKey
    }

    override fun render(delta: Float) {
        if (selectionMade || pauseSupplier.invoke()) {
            return
        }

        buttons[currentButtonKey]?.let { button ->
            getNavigationDirection()?.let {
                onAnyMovement()
                currentButtonKey = button.onNavigate(it, delta)
            }

            if (selectionRequested()) {
                selectionMade = button.onSelect(delta)
            }
        }
    }
}
