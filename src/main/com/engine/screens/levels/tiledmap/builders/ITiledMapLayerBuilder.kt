package com.engine.screens.levels.tiledmap.builders

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObjects
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
