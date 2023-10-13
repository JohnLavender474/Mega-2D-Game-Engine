package com.engine.utils

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.InputAdapter

abstract class GdxTest : InputAdapter(), ApplicationListener {

  override fun create() {}

  override fun resume() {}

  override fun render() {}

  override fun resize(width: Int, height: Int) {}

  override fun pause() {}

  override fun dispose() {}
}
