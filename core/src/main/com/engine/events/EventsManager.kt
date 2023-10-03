package com.engine.events

import com.badlogic.gdx.utils.OrderedSet
import com.badlogic.gdx.utils.Queue

/**
 * A manager for [Event]s and [EventListener]s. [Event]s are submitted to the [EventsManager] and
 * [EventListener]s are added to the [EventsManager]. When an [Event] is submitted, all
 * [EventListener]s are notified of the [Event] when the [run] method is called.
 */
class EventsManager : IEventsManager {

  internal val listeners = OrderedSet<EventListener>()
  internal val eventQueue = Queue<Event>()

  /**
   * Submits an [Event] to this [EventsManager]. The [Event] will be added to the [eventQueue] and
   * will be fired when the [run] method is called.
   */
  override fun queueEvent(event: Event) = eventQueue.addLast(event)

  /**
   * Adds an [EventListener] to this [EventsManager]. The [EventListener] will be notified of
   * [Event]s when the [run] method is called.
   *
   * @param listener The [EventListener] to add.
   * @return If the [EventListener] was added.
   */
  override fun addListener(listener: EventListener) = listeners.add(listener)

  /**
   * Removes an [EventListener] from this [EventsManager]. The [EventListener] will no longer be
   * notified of [Event]s when the [run] method is called.
   *
   * @param listener The [EventListener] to remove.
   * @return If the [EventListener] was removed.
   */
  override fun removeListener(listener: EventListener) = listeners.remove(listener)

  /**
   * Removes all [EventListener]s from this [EventsManager]. The [EventListener]s will no longer be
   * notified of [Event]s when the [run] method is called.
   */
  override fun clearListeners() = listeners.clear()

  /**
   * Notifies all [EventListener]s of [Event]s that have been submitted to this [EventsManager].
   * This method should be called once per game loop.
   */
  override fun run() {
    while (!eventQueue.isEmpty) {
      val event = eventQueue.removeFirst()
      listeners.forEach { it.onEvent(event) }
    }
  }
}
