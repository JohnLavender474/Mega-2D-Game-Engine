package com.mega.game.engine.screens.levels.tiledmap

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile

/**
 * A [TiledMapLevelRenderer] is used to render a [TiledMap] using an [OrthographicCamera].
 * [TiledMapLevelRenderer] extends [OrthogonalTiledMapRenderer] and adds a [render] method that
 * takes an [OrthographicCamera]. In this implementation, the [beginRender] and [endRender] methods
 * are empty and the [dispose] method is a no-op. This is because the [Batch] is managed outside
 * this class; [Batch.dispose], [Batch.begin], and [Batch.end] are called from outside this class.
 *
 * @param map the [TiledMap] to render
 * @param batch the [Batch] to use for rendering
 */
class TiledMapLevelRenderer(map: TiledMap, batch: Batch) : OrthogonalTiledMapRenderer(map, batch) {

    /**
     * Renders the [TiledMap] using the specified [OrthographicCamera].
     *
     * @param camera the [OrthographicCamera] to use for rendering
     */
    fun render(camera: OrthographicCamera) {
        setView(camera)
        super.render()
    }

    override fun beginRender() = AnimatedTiledMapTile.updateAnimationBaseTime()

    override fun endRender() {}

    override fun dispose() {}
}
