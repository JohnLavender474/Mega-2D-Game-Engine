package com.engine.screens.levels.tiledmap.builders

import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.MapObjects
import com.engine.common.objects.Properties

/**
 * A [TiledMapLevelBuilder] is used to build a level from a [Map] of [MapObjects] layers. It is used
 * in conjunction with [ITiledMapLayerBuilder]. The [TiledMapLevelBuilder] will iterate over each
 * layer and call the [ITiledMapLayerBuilder.build] method. The [ITiledMapLayerBuilder.build] method
 * will be passed the [MapObjects] for that layer and the props that are returned from the build
 * method.
 *
 * @param tiledMapLayerBuilders the [Map] of [ITiledMapLayerBuilder]s
 */
class TiledMapLevelBuilder(private val tiledMapLayerBuilders: TiledMapLayerBuilders) {

  /**
   * Builds the level from the layers.
   *
   * @param layers the map layers
   * @return the [Properties] that were built
   */
  fun build(layers: MapLayers): Properties {
    val returnProps = Properties()
    layers.forEach { layer ->
      tiledMapLayerBuilders.layerBuilders[layer.name]?.build(layer, returnProps)
    }
    return returnProps
  }
}
