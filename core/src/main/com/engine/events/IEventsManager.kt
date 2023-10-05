package com.engine.events

interface IEventsManager : Runnable {

  /** Submits an [Event] to this [IEventsManager]. */
  fun queueEvent(event: Event)

  /**
   * Adds an [IEventListener] to this [IEventsManager].
   *
   * @param listener The [IEventListener] to add.
   * @return If the [IEventListener] was added.
   */
  fun addListener(listener: IEventListener): Boolean

  /**
   * Removes an [IEventListener] from this [IEventsManager].
   *
   * @param listener The [IEventListener] to remove.
   * @return If the [IEventListener] was removed.
   */
  fun removeListener(listener: IEventListener): Boolean

  /** Removes all [IEventListener]s from this [IEventsManager]. */
  fun clearListeners()
}
