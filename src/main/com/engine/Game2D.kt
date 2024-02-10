package com.engine

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.engine.common.GameLogger
import com.engine.common.objects.Properties
import com.engine.controller.buttons.Buttons
import com.engine.controller.polling.IControllerPoller
import com.engine.events.EventsManager
import com.engine.events.IEventsManager
import com.engine.screens.IScreen

/**
 * Implementation of [IGame2D] that also derives [Game].
 *
 * @see IGame2D
 * @see Game
 */
abstract class Game2D : IGame2D, Game() {

    companion object {
        const val TAG = "Game2D"
    }

    override lateinit var shapeRenderer: ShapeRenderer
    override lateinit var batch: SpriteBatch
    override lateinit var buttons: Buttons
    override lateinit var controllerPoller: IControllerPoller
    override lateinit var assMan: AssetManager
    override lateinit var eventsMan: IEventsManager
    override lateinit var gameEngine: IGameEngine

    override var paused = false

    override val screens = ObjectMap<String, IScreen>()
    override val viewports = ObjectMap<String, Viewport>()
    override val currentScreen: IScreen?
        get() = currentScreenKey?.let { screens[it] }

    override val properties = Properties()

    var currentScreenKey: String? = null

    /**
     * Creates the [IControllerPoller] used by this game.
     *
     * @return the controller poller
     */
    protected abstract fun defineControllerPoller(): IControllerPoller

    /**
     * Loads the assets into the [AssetManager].
     *
     * @param assMan the asset manager to load assets into.
     */
    protected abstract fun loadAssets(assMan: AssetManager)

    /**
     * Creates the [IGameEngine].
     *
     * @return the game engine
     */
    protected abstract fun createGameEngine(): IGameEngine

    /**
     * Hides the old screen and removes it from the events manager. After that, if there is a screen
     * mapped to the specified key, then that screen is shown, resized, and added as a listener to the
     * events manager.
     *
     * @param key the key
     */
    override fun setCurrentScreen(key: String) {
        GameLogger.debug(TAG, "setCurrentScreen: set to screen with key = $key")

        // hide old screen and remove it from events manager
        currentScreenKey
            ?.let { screens[it] }
            ?.let {
                it.hide()
                eventsMan.removeListener(it)
            }

        // set next screen key
        currentScreenKey = key

        // get next screen, and if present show it, resize it, add it as an events listener, and pause
        // it if necessary
        screens[key]?.let { nextScreen ->
            nextScreen.show()
            nextScreen.resize(Gdx.graphics.width, Gdx.graphics.height)

            eventsMan.addListener(nextScreen)

            if (paused) nextScreen.pause()
        }
    }

    /**
     * Initializes the sprite batch, the shape renderer (with auto shape type set to true), the
     * buttons, the controller poller, the asset manager, the audio manager, the events manager, and
     * the game engine. Also calls [loadAssets] to load assets into the asset manager.
     */
    override fun create() {
        GameLogger.debug(TAG, "create()")
        shapeRenderer = ShapeRenderer()
        shapeRenderer.setAutoShapeType(true)
        batch = SpriteBatch()
        controllerPoller = defineControllerPoller()
        assMan = AssetManager()
        loadAssets(assMan)
        assMan.finishLoading()
        eventsMan = EventsManager()
        gameEngine = createGameEngine()
    }

    /**
     * Resizes the viewports and current screen.
     *
     * @param width the width
     * @param height the height
     */
    override fun resize(width: Int, height: Int) {
        viewports.values().forEach { it.update(width, height) }
        currentScreen?.resize(width, height)
    }

    /**
     * Clears the screen; updates the controller poller, and events manager; renders the current
     * screen; and updates all the viewports contained in [viewports]
     */
    override fun render() {
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1f)
        Gdx.graphics.gL20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val delta = Gdx.graphics.deltaTime

        controllerPoller.run()
        eventsMan.run()

        currentScreen?.render(delta)
        viewports.values().forEach { it.apply() }
    }

    /** Pauses the current screen and sets [paused] to true. */
    override fun pause() {
        GameLogger.debug(TAG, "pause()")

        if (paused) return

        paused = true
        currentScreen?.pause()
    }

    /** Resumes the current screen and sets [paused] to false. */
    override fun resume() {
        GameLogger.debug(TAG, "resume()")

        if (!paused) return

        paused = false
        currentScreen?.resume()
    }

    /** Disposes of the [batch], [shapeRenderer], and [IScreen]s. */
    override fun dispose() {
        GameLogger.debug(TAG, "dispose()")

        batch.dispose()
        shapeRenderer.dispose()
        screens.values().forEach { it.dispose() }
    }
}
