package com.engine

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.engine.common.objects.Properties
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
 */
interface IGame2D : ApplicationListener {

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

    val properties: Properties

    /**
     * Sets the current screen using the key.
     *
     * @param key the key
     */
    fun setCurrentScreen(key: String)
}
