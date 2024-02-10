package com.engine.cullables

import com.badlogic.gdx.utils.ObjectSet
import com.engine.common.GameLogger
import com.engine.events.Event
import com.engine.events.IEventListener

/**
 * A [ICullable] that will be culled if [cullOnEvent] returns true for any event.
 *
 * @param cullOnEvent a function that returns true if the [ICullable] should be culled for the
 *   event.
 */
class CullableOnEvent(
    private val cullOnEvent: (Event) -> Boolean,
    override val eventKeyMask: ObjectSet<Any> = ObjectSet()
) : ICullable, IEventListener {

    companion object {
        const val TAG = "CullableOnEvent"
    }

    private var cull: Boolean = false

    override fun shouldBeCulled(delta: Float) = cull

    override fun onEvent(event: Event) {
        GameLogger.debug(TAG, "onEvent(): event = $event")
        if (!cull && cullOnEvent(event)) {
            GameLogger.debug(TAG, "Now culling on event = $event")
            cull = true
        }
    }

    override fun reset() {
        GameLogger.debug(TAG, "Resetting CullableOnEvent")
        cull = false
    }

    override fun toString() = "CullableOnEvent"
}
