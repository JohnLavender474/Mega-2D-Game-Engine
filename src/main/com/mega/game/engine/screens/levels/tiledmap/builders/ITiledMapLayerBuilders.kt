package com.mega.game.engine.screens.levels.tiledmap.builders

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.objects.Properties

/**
 * A [TiledMapLayerBuilders] is used to build a [TiledMap] of [MapLayer]s. It is used in conjunction
 * with [ITiledMapLayerBuilder]. The [TiledMapLayerBuilders] will iterate over each layer and call
 * the [ITiledMapLayerBuilder.build] method.
 */
abstract class TiledMapLayerBuilders {

    companion object {
        const val TAG = "TiledMapLayerBuilders"
    }

    /**
     * The [Map] of [ITiledMapLayerBuilder]s. The key is the value of [MapLayer.name] and the value is
     * the [ITiledMapLayerBuilder]. This map should be populated before calling [build].
     */
    abstract val layerBuilders: OrderedMap<String, ITiledMapLayerBuilder>

    /**
     * Builds all layers of the [map] by building to the provided [Properties].
     *
     * @param layers the [MapLayers] to build.
     * @param returnProps the [Properties] to use as a return container.
     */
    open fun build(layers: MapLayers, returnProps: Properties) {
        val layersMap = layers.associateBy { it.name }
        layerBuilders.forEach {
            val layer = layersMap[it.key]
            if (layer != null) it.value.build(layer, returnProps)
        }
    }
}
