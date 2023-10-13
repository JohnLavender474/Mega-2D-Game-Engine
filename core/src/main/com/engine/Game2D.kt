package com.engine

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.engine.audio.AudioManager
import com.engine.audio.IAudioManager
import com.engine.common.objects.Properties
import com.engine.controller.buttons.Buttons
import com.engine.controller.polling.ControllerPoller
import com.engine.controller.polling.IControllerPoller
import com.engine.events.EventsManager
import com.engine.events.IEventsManager

abstract class Game2D : IGame2D, Game() {

  override lateinit var batch: SpriteBatch
  override lateinit var shapeRenderer: ShapeRenderer

  override lateinit var buttons: Buttons
  override lateinit var controllerPoller: IControllerPoller

  override lateinit var assMan: AssetManager
  override lateinit var audioMan: IAudioManager
  override lateinit var eventsMan: IEventsManager

  override lateinit var gameEngine: IGameEngine

  override val screens = ObjectMap<String, Screen>()
  override val viewports = ObjectMap<String, Viewport>()
  override val currentScreen: Screen?
    get() = currentScreenKey?.let { screens[it] }

  override val properties = Properties()

  var paused = false
  var currentScreenKey: String? = null

  protected abstract fun createButtons(): Buttons

  protected abstract fun loadAssets(assMan: AssetManager)

  protected abstract fun createGameEngine(): IGameEngine

  override fun setCurrentScreen(key: String) {
    currentScreenKey?.let { screens[it] }?.hide()
    currentScreenKey = key

    screens[key]?.let { nextScreen ->
      nextScreen.show()
      nextScreen.resize(Gdx.graphics.width, Gdx.graphics.height)

      if (paused) {
        nextScreen.pause()
      }
    }
  }

  override fun create() {
    batch = SpriteBatch()
    shapeRenderer = ShapeRenderer()

    buttons = createButtons()
    controllerPoller = ControllerPoller(buttons)

    assMan = AssetManager()
    loadAssets(assMan)
    audioMan = AudioManager(assMan)
    eventsMan = EventsManager()

    gameEngine = createGameEngine()
  }

  override fun resize(width: Int, height: Int) {
    viewports.values().forEach { it.update(width, height) }
    currentScreen?.resize(width, height)
  }

  override fun render() {
    Gdx.gl20.glClearColor(0f, 0f, 0f, 1f)
    Gdx.graphics.gL20.glClear(GL20.GL_COLOR_BUFFER_BIT)

    val delta = Gdx.graphics.deltaTime

    audioMan.update(delta)
    controllerPoller.run()
    eventsMan.run()

    currentScreen?.render(delta)
    viewports.values().forEach { it.apply() }
  }

  override fun pause() {
    if (paused) {
      return
    }

    paused = true
    currentScreen?.pause()
  }

  override fun resume() {
    if (!paused) {
      return
    }

    paused = false
    currentScreen?.resume()
  }

  override fun dispose() {
    batch.dispose()
    shapeRenderer.dispose()
    screens.values().forEach { it.dispose() }
  }
}
