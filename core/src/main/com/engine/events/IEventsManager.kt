package com.engine.events

interface IEventsManager : Runnable {

  /** Submits an [Event] to this [IEventsManager]. */
  fun queueEvent(event: Event)

  /**
   * Adds an [EventListener] to this [IEventsManager].
   *
   * @param listener The [EventListener] to add.
   * @return If the [EventListener] was added.
   */
  fun addListener(listener: EventListener): Boolean

  /**
   * Removes an [EventListener] from this [IEventsManager].
   *
   * @param listener The [EventListener] to remove.
   * @return If the [EventListener] was removed.
   */
  fun removeListener(listener: EventListener): Boolean

  /** Removes all [EventListener]s from this [IEventsManager]. */
  fun clearListeners()
}
