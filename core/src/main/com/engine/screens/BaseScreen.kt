package com.engine.screens

import com.badlogic.gdx.utils.ObjectSet
import com.engine.IGame2D
import com.engine.common.objects.Properties
import com.engine.events.Event

/** A [BaseScreen] is a basic implementation of [IScreen]. */
open class BaseScreen(
    protected val game: IGame2D,
    override val properties: Properties = Properties()
) : IScreen {

    override val eventKeyMask = ObjectSet<Any>()

    override fun show() {}

    override fun render(delta: Float) {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}

    override fun onEvent(event: Event) {}
}
