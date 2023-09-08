package com.engine.cullables

import com.engine.events.Event
import com.engine.events.EventListener

/**
 * A [Cullable] that will be culled if [cullOnEvent] returns true for any event.
 *
 * @param cullOnEvent a function that returns true if the [Cullable] should be culled for the event.
 */
class CullableOnEvent(private val cullOnEvent: (Event) -> Boolean) : Cullable, EventListener {

  private var cull: Boolean = false

  override fun shouldBeCulled() = cull

  override fun onEvent(event: Event) {
    if (!cull && cullOnEvent(event)) {
      cull = true
    }
  }

  override fun reset() {
    cull = false
  }
}
