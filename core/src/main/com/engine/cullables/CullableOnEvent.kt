package com.engine.cullables

import com.badlogic.gdx.utils.ObjectSet
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
