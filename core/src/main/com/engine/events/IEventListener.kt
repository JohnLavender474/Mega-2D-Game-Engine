package com.engine.events

/** An interface for event listeners. Event listeners are called when an [Event] is fired. */
fun interface IEventListener {

  /**
   * Called when an [Event] is fired.
   *
   * @param event the [Event] that was fired
   */
  fun onEvent(event: Event)
}
