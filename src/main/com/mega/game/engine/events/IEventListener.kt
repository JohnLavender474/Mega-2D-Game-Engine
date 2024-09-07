package com.mega.game.engine.events

import com.badlogic.gdx.utils.ObjectSet

/**
 * An interface for event listeners. Event listeners are called when an [Event] is fired.
 */
interface IEventListener {

    /**
     * A set of [Event] keys that this [IEventListener] will be notified of.
     */
    val eventKeyMask: ObjectSet<Any>

    /**
     * Called when an [Event] is fired.
     *
     * @param event the [Event] that was fired
     */
    fun onEvent(event: Event)
}
