package com.engine.screens

import com.engine.common.objects.Properties

/** A [BaseScreen] is a basic abstract implementation of [IScreen] with no-op methods for each method. */
abstract class BaseScreen(override val properties: Properties = Properties()) : IScreen {

    override fun show() {}

    override fun render(delta: Float) {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}
