package com.engine.screens.levels.tiledmap

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ObjectMap
import com.engine.Game2D
import com.engine.screens.levels.LevelScreen

/**
 * A [TiledMapLevelScreen] is a [LevelScreen] that loads a tiled map and builds the layers using a
 * map of [TiledMapLayerBuilder]s.
 *
 * @param game the game
 * @param tmxSrc the path to the tmx file
 */
abstract class TiledMapLevelScreen(protected val game: Game2D, protected val tmxSrc: String) :
    LevelScreen {

  protected lateinit var tiledMapLoadResult: TiledMapLoadResult
  protected lateinit var tiledMapLevelRenderer: TiledMapLevelRenderer

  /**
   * The map of layer names to [TiledMapLayerBuilder]s.
   *
   * @see TiledMapLayerBuilder
   */
  protected abstract fun getLayerBuilders(): ObjectMap<String, TiledMapLayerBuilder>

  /**
   * Calls the [TiledMapLevelRenderer] using the specified [OrthographicCamera].
   *
   * @param camera the [OrthographicCamera] to use for rendering
   */
  protected fun render(camera: OrthographicCamera) = tiledMapLevelRenderer.render(camera)

  override fun show() {
    tiledMapLoadResult = TiledMapLevelLoader.load(tmxSrc)
    TiledMapLevelBuilder.build(tiledMapLoadResult.layers, getLayerBuilders())

    tiledMapLevelRenderer = TiledMapLevelRenderer(tiledMapLoadResult.map, game.batch)
  }

  override fun dispose() {
    tiledMapLoadResult.map.dispose()
  }
}
