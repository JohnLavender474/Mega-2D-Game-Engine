package com.engine

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import com.engine.assets.GameAssetManager
import com.engine.audio.AudioManager
import com.engine.common.interfaces.Propertizable
import com.engine.common.objects.Properties
import com.engine.controller.ControllerPoller
import com.engine.events.EventsManager

/** The main class of the game. */
abstract class Game2D : ApplicationListener, Propertizable {

  lateinit var batch: SpriteBatch
    protected set

  lateinit var shapeRenderer: ShapeRenderer
    protected set

  private val disposables = Array<Disposable>()

  var currentScreenKey: String? = null
  val currentScreen: Screen?
    get() = currentScreenKey?.let { getScreen(it) }

  val screens = HashMap<String, Screen>()
  val viewports = HashMap<String, Viewport>()

  var paused = false

  lateinit var assetManager: GameAssetManager
    protected set

  lateinit var audioManager: AudioManager
    protected set

  lateinit var eventsManager: EventsManager
    protected set

  lateinit var controllerPoller: ControllerPoller
    protected set

  lateinit var gameEngine: GameEngine
    protected set

  override val properties: Properties = Properties()

  /**
   * Creates the [GameAssetManager] for this [Game2D].
   *
   * @return the [GameAssetManager] for this [Game2D]
   */
  protected abstract fun createAssetManager(): GameAssetManager

  /**
   * Creates the [AudioManager] for this [Game2D].
   *
   * @return the [AudioManager] for this [Game2D]
   */
  protected abstract fun createAudioManager(): AudioManager

  /**
   * Creates the [EventsManager] for this [Game2D].
   *
   * @return the [EventsManager] for this [Game2D]
   */
  protected abstract fun createEventManager(): EventsManager

  /**
   * Creates the [ControllerPoller] for this [Game2D].
   *
   * @return the [ControllerPoller] for this [Game2D]
   */
  protected abstract fun createControllerPoller(): ControllerPoller

  /**
   * Creates the [GameEngine] for this [Game2D].
   *
   * @return the [GameEngine] for this [Game2D]
   */
  protected abstract fun createGameEngine(): GameEngine

  /**
   * Adds the given [Disposable] to the [disposables] list.
   *
   * @param disposable the [Disposable] to add
   * @return the [Disposable] that was added
   */
  fun addDisposable(disposable: Disposable) = disposables.add(disposable)

  /**
   * Gets the [Screen] with the given key.
   *
   * @param key the key of the [Screen] to get
   * @return the [Screen] with the given key
   */
  fun getScreen(key: String) = screens[key]

  /**
   * Puts the given [Screen] into the [screens] map with the given key.
   *
   * @param key the key to put the [Screen] into
   * @param screen the [Screen] to put into the map
   * @return the [Screen] that was put into the map
   */
  fun putScreen(key: String, screen: Screen) = screens.put(key, screen)

  /**
   * Sets the current screen to the [Screen] with the given key. If the current screen is not null,
   * it will be hidden. If the current screen is not null and the game is paused, the current screen
   * will be paused. If the [Screen] with the given key is not null, it will be shown and resized.
   * If the game is paused, the [Screen] with the given key will be paused.
   *
   * @param key the key of the [Screen] to set as the current screen
   */
  fun setCurrentScreen(key: String?) {
    currentScreenKey?.let { getScreen(it) }?.hide()
    currentScreenKey = key

    key?.let {
      getScreen(it)?.let { nextScreen ->
        nextScreen.show()
        nextScreen.resize(Gdx.graphics.width, Gdx.graphics.height)

        if (paused) {
          nextScreen.pause()
        }
      }
    }
  }

  override fun create() {
    batch = SpriteBatch()
    shapeRenderer = ShapeRenderer()

    assetManager = createAssetManager()
    audioManager = createAudioManager()
    eventsManager = createEventManager()
    controllerPoller = createControllerPoller()
    gameEngine = createGameEngine()

    disposables.add(batch)
    disposables.add(shapeRenderer)
    disposables.add(assetManager)
  }

  override fun resize(width: Int, height: Int) {
    viewports.values.forEach { it.update(width, height) }
    currentScreen?.resize(width, height)
  }

  override fun render() {
    val delta = Gdx.graphics.deltaTime

    audioManager.update(delta)

    controllerPoller.run()
    eventsManager.run()

    currentScreen?.render(delta)

    viewports.values.forEach { it.apply() }
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
    screens.values.forEach { it.dispose() }
    screens.clear()
    disposables.forEach { it.dispose() }
    disposables.clear()
  }
}
