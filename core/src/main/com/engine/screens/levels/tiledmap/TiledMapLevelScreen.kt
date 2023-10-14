package com.engine.screens.levels.tiledmap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ObjectMap
import com.engine.IGame2D
import com.engine.common.objects.Properties
import com.engine.screens.BaseScreen
import com.engine.screens.IScreen

/**
 * A [TiledMapLevelScreen] is a [IScreen] that loads a tiled map and builds the layers using a map
 * of [ITiledMapLayerBuilder]s. This screen is intended to be reused for multiple levels. Make sure
 * that the [tmxMapSource] is set each time to the correct level before calling [show].
 *
 * @param game the [IGame2D] instance to use for this [TiledMapLevelScreen]
 * @param properties the [Properties] to use for this [TiledMapLevelScreen]
 * @property tmxMapSource the source of the tiled map to load. This should be set to the level
 *   intended to be rendered each time before calling [show].
 * @property tiledMapLoadResult the [TiledMapLoadResult] that was loaded from
 *   [TiledMapLevelLoader.load]. This is set in [show].
 * @property tiledMapLevelRenderer the [TiledMapLevelRenderer] that was created from the
 *   [TiledMapLoadResult] and the [SpriteBatch]. This is set in [show]. This is used to render the
 *   tiled map and should be called somewhere in the deriving class.
 */
abstract class TiledMapLevelScreen(game: IGame2D, properties: Properties) :
    BaseScreen(game, properties) {

  var tmxMapSource: String? = null

  protected var tiledMapLoadResult: TiledMapLoadResult? = null
  protected var tiledMapLevelRenderer: TiledMapLevelRenderer? = null

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
   * Loads the tiled map and builds the level. This method must be called after the [tmxMapSource]
   * is set. If the [tmxMapSource] is not set, an [IllegalStateException] will be thrown. This
   * method loads the map using a [TiledMapLevelLoader], and the field [tiledMapLoadResult] is set
   * to the result of the load. Then, a [TiledMapLevelBuilder] is created using the layer builders
   * returned by [getLayerBuilders]. The level is built using the [TiledMapLevelBuilder] and the
   * result is passed to [buildLevel]. Finally, a [TiledMapLevelRenderer] is created using the
   * [TiledMapLoadResult] and the [SpriteBatch].
   *
   * @throws IllegalStateException if the [tmxMapSource] is not set
   */
  override fun show() =
      tmxMapSource?.let {
        tiledMapLoadResult = TiledMapLevelLoader.load(it)
        val builder = TiledMapLevelBuilder(getLayerBuilders())
        val result = builder.build(tiledMapLoadResult!!.map.layers)
        buildLevel(result)
        tiledMapLevelRenderer = TiledMapLevelRenderer(tiledMapLoadResult!!.map, game.batch)
      } ?: throw IllegalStateException("Tmx map source must be set before calling show()")

  /** Disposes the tiled map. */
  override fun dispose() {
    tiledMapLoadResult?.map?.dispose()
  }
}
