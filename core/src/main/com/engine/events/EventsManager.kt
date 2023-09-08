package com.engine.events

import java.util.*

/**
 * A manager for [Event]s and [EventListener]s. [Event]s are submitted to the [EventsManager] and
 * [EventListener]s are added to the [EventsManager]. When an [Event] is submitted, all
 * [EventListener]s are notified of the [Event] when the [run] method is called.
 */
class EventsManager : Runnable {

  internal val listeners = LinkedHashSet<EventListener>()
  internal val eventQueue = LinkedList<Event>()

  /**
   * Submits an [Event] to this [EventsManager]. The [Event] will be added to the [eventQueue] and
   * will be fired when the [run] method is called.
   */
  fun queueEvent(event: Event) = eventQueue.add(event)

  /**
   * Adds an [EventListener] to this [EventsManager]. The [EventListener] will be notified of
   * [Event]s when the [run] method is called.
   *
   * @param listener The [EventListener] to add.
   * @return If the [EventListener] was added.
   */
  fun addListener(listener: EventListener) = listeners.add(listener)

  /**
   * Removes an [EventListener] from this [EventsManager]. The [EventListener] will no longer be
   * notified of [Event]s when the [run] method is called.
   *
   * @param listener The [EventListener] to remove.
   * @return If the [EventListener] was removed.
   */
  fun removeListener(listener: EventListener) = listeners.remove(listener)

  /**
   * Removes all [EventListener]s from this [EventsManager]. The [EventListener]s will no longer be
   * notified of [Event]s when the [run] method is called.
   */
  fun clearListeners() = listeners.clear()

  /**
   * Notifies all [EventListener]s of [Event]s that have been submitted to this [EventsManager]. This
   * method should be called once per game loop.
   */
  override fun run() {
    while (eventQueue.isNotEmpty()) {
      val event = eventQueue.poll()
      listeners.forEach { it.onEvent(event) }
    }
  }
}
