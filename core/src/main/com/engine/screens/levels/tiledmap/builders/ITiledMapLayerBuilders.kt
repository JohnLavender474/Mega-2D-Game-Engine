package com.engine.screens.levels.tiledmap.builders

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties

/**
 * A [TiledMapLayerBuilders] is used to build a [TiledMap] of [MapLayer]s. It is used in conjunction
 * with [ITiledMapLayerBuilder]. The [TiledMapLayerBuilders] will iterate over each layer and call
 * the [ITiledMapLayerBuilder.build] method. The [ITiledMapLayerBuilder.build] method will be passed
 * the [MapLayer] for that layer and the props that are returned from the build method.
 */
abstract class TiledMapLayerBuilders {

  /**
   * The [Map] of [ITiledMapLayerBuilder]s. The key is the value of [MapLayer.name] and the value is
   * the [ITiledMapLayerBuilder]. This map should be populated before calling [build].
   */
  abstract val layerBuilders: ObjectMap<String, ITiledMapLayerBuilder>

  /**
   * Builds all layers of the [map] and returns the [Properties] that were built.
   *
   * @param layers the [MapLayers] to build.
   * @return the [Properties] that were built.
   */
  open fun build(layers: MapLayers): Properties {
    val returnProps = Properties()
    layers.forEach { layer -> layerBuilders[layer.name]?.build(layer, returnProps) }
    return returnProps
  }
}
