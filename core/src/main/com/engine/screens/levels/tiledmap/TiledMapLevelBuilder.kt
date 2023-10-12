package com.engine.screens.levels.tiledmap

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties

/**
 * A [ITiledMapLayerBuilder] is used to build a [MapObjects] layer. It is used in conjunction with
 * [TiledMapLevelBuilder]. The [TiledMapLevelBuilder] will iterate over each layer and call the
 * [ITiledMapLayerBuilder.build] method. The [ITiledMapLayerBuilder.build] method will be passed the
 * [MapObjects] for that layer and the props that are returned from the build method.
 *
 * @see TiledMapLevelBuilder
 */
fun interface ITiledMapLayerBuilder {

  /**
   * Builds the [MapLayer] for a layer.
   *
   * @param layer the [MapLayer].
   * @param returnProps the [Properties] that will be returned from the [TiledMapLevelBuilder]
   */
  fun build(layer: MapLayer, returnProps: Properties)
}

/**
 * A [TiledMapLevelBuilder] is used to build a level from a [Map] of [MapObjects] layers. It is used
 * in conjunction with [ITiledMapLayerBuilder]. The [TiledMapLevelBuilder] will iterate over each
 * layer and call the [ITiledMapLayerBuilder.build] method. The [ITiledMapLayerBuilder.build] method
 * will be passed the [MapObjects] for that layer and the props that are returned from the build
 * method.
 *
 * @param layerBuilders the [Map] of [ITiledMapLayerBuilder]s
 */
class TiledMapLevelBuilder(private val layerBuilders: ObjectMap<String, ITiledMapLayerBuilder>) {

  /**
   * Builds the level from the layers.
   *
   * @param layers the map layers
   * @return the [Properties] that were built
   */
  fun build(layers: MapLayers): Properties {
    val returnProps = Properties()
    layers.forEach { layer -> layerBuilders[layer.name]?.build(layer, returnProps) }
    return returnProps
  }
}
