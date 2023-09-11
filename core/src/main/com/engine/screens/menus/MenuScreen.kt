package com.engine.screens.menus

import com.badlogic.gdx.Screen
import com.engine.Game2D
import com.engine.common.enums.Direction

/**
 * Abstract class for menu screens. Handles navigation and selection of buttons. The first button is
 * selected by default.
 *
 * @param game the game instance
 * @param firstButtonKey the key of the first button to be selected
 */
abstract class MenuScreen(protected val game: Game2D, protected val firstButtonKey: String) :
    Screen {

  protected val buttons = HashMap<String, MenuButton>()

  var currentButtonKey: String? = firstButtonKey
  var selectionMade = false
    private set

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
    if (selectionMade || game.paused) {
      return
    }

    buttons[currentButtonKey]?.let { button ->
      getNavigationDirection()?.let {
        onAnyMovement()
        button.onNavigate(it, delta)
      }

      if (selectionRequested()) {
        selectionMade = button.onSelect(delta)
      }
    }
  }
}
