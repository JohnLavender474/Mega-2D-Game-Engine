package com.mega.game.engine.screens.levels.tiledmap

import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.ObjectMap

/**
 * The result of loading a [TiledMap] using [TiledMapLevelLoader].
 *
 * @param map the [TiledMap]
 * @param layers the map of layer names to [MapObjects]
 * @param worldWidth the width of the world
 * @param worldHeight the height of the world
 */
data class TiledMapLoadResult(
    val map: TiledMap,
    val layers: ObjectMap<String, MapObjects>,
    val worldWidth: Int,
    val worldHeight: Int
)

/**
 * A [TiledMapLevelLoader] is used to load a [TiledMap] from a tmx file.
 *
 * @see TiledMap
 */
object TiledMapLevelLoader {

    /**
     * Loads a [TiledMap] from a tmx file.
     *
     * @param tmxSrc the path to the tmx file
     * @return the map of layer names to [MapObjects]
     */
    fun load(tmxSrc: String): TiledMapLoadResult {
        val map = TmxMapLoader().load(tmxSrc) ?: throw IllegalStateException("Failed to load map: $tmxSrc")

        val worldWidth = map.properties["width"] as Int
        val worldHeight = map.properties["height"] as Int

        val layers = ObjectMap<String, MapObjects>()
        map.layers.forEach { layer -> layers.put(layer.name, layer.objects) }
        return TiledMapLoadResult(map, layers, worldWidth, worldHeight)
    }
}
