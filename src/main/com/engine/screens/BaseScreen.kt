package com.engine.screens

import com.engine.common.objects.Properties
import com.engine.events.Event

/** A [BaseScreen] is a basic abstract implementation of [IScreen]. */
abstract class BaseScreen(
    override val properties: Properties = Properties()
) : IScreen {

    protected open var initialized = false

    override fun init() {}

    /**
     * Shows the screen. If [initialized] is false, then [init] will be called and [initialized] will be set to true.
     * If [initialized] is true, then [init] is not called.
     */
    override fun show() {
        if (!initialized) {
            init()
            initialized = true
        }
    }

    override fun render(delta: Float) {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}

    override fun onEvent(event: Event) {}
}
