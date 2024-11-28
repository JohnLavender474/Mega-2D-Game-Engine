package com.mega.game.engine.screens.levels.tiledmap

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.props
import com.mega.game.engine.screens.BaseScreen
import com.mega.game.engine.screens.IScreen
import com.mega.game.engine.screens.levels.tiledmap.builders.ITiledMapLayerBuilder
import com.mega.game.engine.screens.levels.tiledmap.builders.TiledMapLayerBuilders


abstract class TiledMapLevelScreen(
    val batch: Batch,
    var tmxMapSource: String? = null,
    properties: Properties = props()
) : BaseScreen(properties) {


    protected var tiledMapLoadResult: TiledMapLoadResult? = null


    protected var tiledMapLevelRenderer: TiledMapLevelRenderer? = null


    protected val tiledMap: TiledMap?
        get() = tiledMapLoadResult?.map


    protected abstract fun getLayerBuilders(): TiledMapLayerBuilders


    protected abstract fun buildLevel(result: Properties)


    override fun show() {
        super.show()
        tmxMapSource?.let {
            tiledMapLoadResult = TiledMapLevelLoader.load(it)
            val returnProps = Properties()
            val layerBuilders = getLayerBuilders()
            layerBuilders.build(tiledMapLoadResult!!.map.layers, returnProps)
            buildLevel(returnProps)
            tiledMapLevelRenderer = TiledMapLevelRenderer(tiledMapLoadResult!!.map, batch)
        } ?: throw IllegalStateException("Tmx map source must be set before calling show()")
    }


    override fun reset() {
        tiledMap?.dispose()
    }


    override fun dispose() {
        tiledMap?.dispose()
    }
}
