package com.engine.screens.levels.tiledmap

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties
import com.engine.screens.levels.ILevelScreen

/**
 * A [TiledMapLevelScreen] is a [ILevelScreen] that loads a tiled map and builds the layers using a
 * map of [ITiledMapLayerBuilder]s.
 *
 * @param batch the sprite batch
 * @param tmxSrc the path to the tmx file
 */
abstract class TiledMapLevelScreen(protected val batch: SpriteBatch, protected val tmxSrc: String) :
    ILevelScreen {

  protected lateinit var tiledMapLoadResult: TiledMapLoadResult
  protected lateinit var tiledMapLevelRenderer: TiledMapLevelRenderer

  /**
   * The map of layer names to [ITiledMapLayerBuilder]s.
   *
   * @see ITiledMapLayerBuilder
   */
  protected abstract fun getLayerBuilders(): ObjectMap<String, ITiledMapLayerBuilder>

  /**
   * Builds the level using the specified [Properties].
   *
   * @param result the [Properties] that were built
   */
  protected abstract fun buildLevel(result: Properties)

  /**
   * Calls the [TiledMapLevelRenderer] using the specified [OrthographicCamera].
   *
   * @param camera the [OrthographicCamera] to use for rendering
   */
  protected fun renderLevelMap(camera: OrthographicCamera) = tiledMapLevelRenderer.render(camera)

  override fun show() {
    tiledMapLoadResult = TiledMapLevelLoader.load(tmxSrc)
    val builder = TiledMapLevelBuilder(getLayerBuilders())
    val result = builder.build(tiledMapLoadResult.map.layers)
    buildLevel(result)
    tiledMapLevelRenderer = TiledMapLevelRenderer(tiledMapLoadResult.map, batch)
  }

  override fun dispose() {
    tiledMapLoadResult.map.dispose()
  }
}
