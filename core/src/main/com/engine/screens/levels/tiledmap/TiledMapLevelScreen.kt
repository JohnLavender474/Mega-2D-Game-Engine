package com.engine.screens.levels.tiledmap

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ObjectMap
import com.engine.screens.levels.ILevelScreen

/**
 * A [TiledMapLevelScreen] is a [ILevelScreen] that loads a tiled map and builds the layers using a
 * map of [TiledMapLayerBuilder]s.
 *
 * @param batch the sprite batch
 * @param tmxSrc the path to the tmx file
 */
abstract class TiledMapLevelScreen(protected val batch: SpriteBatch, protected val tmxSrc: String) :
    ILevelScreen {

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

    tiledMapLevelRenderer = TiledMapLevelRenderer(tiledMapLoadResult.map, batch)
  }

  override fun dispose() {
    tiledMapLoadResult.map.dispose()
  }
}
