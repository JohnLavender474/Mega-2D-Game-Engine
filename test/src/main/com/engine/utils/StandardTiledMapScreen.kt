package com.engine.utils

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.engine.common.extensions.objectMapOf
import com.engine.common.objects.Properties
import com.engine.events.Event
import com.engine.screens.levels.tiledmap.ITiledMapLayerBuilder
import com.engine.screens.levels.tiledmap.TiledMapLevelScreen

class StandardTiledMapScreen(
    batch: SpriteBatch,
    tmxSrc: String,
    private val layerBuilders: ObjectMap<String, ITiledMapLayerBuilder> = objectMapOf()
) : TiledMapLevelScreen(batch, tmxSrc) {

  override val eventKeyMask = ObjectSet<Any>()

  override fun getLayerBuilders() = layerBuilders

  override fun buildLevel(result: Properties) {
    TODO("Not yet implemented")
  }

  override fun render(delta: Float) {
    TODO("Not yet implemented")
  }

  override fun resize(width: Int, height: Int) {
    TODO("Not yet implemented")
  }

  override fun pause() {
    TODO("Not yet implemented")
  }

  override fun resume() {
    TODO("Not yet implemented")
  }

  override fun hide() {
    TODO("Not yet implemented")
  }

  override fun onEvent(event: Event) {
    TODO("Not yet implemented")
  }
}
