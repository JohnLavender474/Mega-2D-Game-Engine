package com.engine

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.engine.common.interfaces.IPropertizable
import com.engine.controller.buttons.Buttons
import com.engine.controller.polling.IControllerPoller
import com.engine.events.IEventsManager
import com.engine.screens.IScreen

/**
 * Interface for the game object. The game is the main class that initializes the game and houses
 * all the components necessary to make and run a game. The game object is responsible for
 * initializing the game engine, the asset manager, the audio manager, the events manager, the
 * controller poller, the buttons, the screens, and the viewports. The game object is also
 * responsible for setting and rendering screens.
 *
 * If implementing this interface in a Java class, then you will need to override the getters for
 * the fields rather than the field declarations themselves. In that case, it is highly recommended
 * (for some fields basically mandatory) that each getter (1) either instantiate the field in an
 * initialization method (such as the create method in LibGDX's Game class) or lazily initialize
 * the field when the method is first called and (2) return the same instance on every getter call.
 */
interface IGame2D : ApplicationListener, IPropertizable {

    var batch: SpriteBatch
    var shapeRenderer: ShapeRenderer

    var buttons: Buttons
    var controllerPoller: IControllerPoller

    var assMan: AssetManager
    var eventsMan: IEventsManager

    var engine: IGameEngine

    var paused: Boolean

    val screens: ObjectMap<String, IScreen>
    val viewports: ObjectMap<String, Viewport>
    val currentScreen: IScreen?

    /**
     * Sets the current screen using the key. Should fetch the screen from the [screens] map and show it.
     *
     * @param key the key of the screen to show.
     */
    fun setCurrentScreen(key: String)
}
