package com.engine.events

import com.badlogic.gdx.utils.OrderedSet
import com.badlogic.gdx.utils.Queue

/**
 * A manager for [Event]s and [IEventListener]s. [Event]s are submitted to the [EventsManager] and
 * [IEventListener]s are added to the [EventsManager]. When an [Event] is submitted, all
 * [IEventListener]s are notified of the [Event] when the [run] method is called.
 */
class EventsManager : IEventsManager {

  internal val listeners = OrderedSet<IEventListener>()
  internal val eventQueue = Queue<Event>()

  /**
   * Submits an [Event] to this [EventsManager]. The [Event] will be added to the [eventQueue] and
   * will be fired when the [run] method is called.
   */
  override fun queueEvent(event: Event) = eventQueue.addLast(event)

  /**
   * Adds an [IEventListener] to this [EventsManager]. The [IEventListener] will be notified of
   * [Event]s when the [run] method is called.
   *
   * @param listener The [IEventListener] to add.
   * @return If the [IEventListener] was added.
   */
  override fun addListener(listener: IEventListener) = listeners.add(listener)

  /**
   * Removes an [IEventListener] from this [EventsManager]. The [IEventListener] will no longer be
   * notified of [Event]s when the [run] method is called.
   *
   * @param listener The [IEventListener] to remove.
   * @return If the [IEventListener] was removed.
   */
  override fun removeListener(listener: IEventListener) = listeners.remove(listener)

  /**
   * Removes all [IEventListener]s from this [EventsManager]. The [IEventListener]s will no longer be
   * notified of [Event]s when the [run] method is called.
   */
  override fun clearListeners() = listeners.clear()

  /**
   * Notifies all [IEventListener]s of [Event]s that have been submitted to this [EventsManager].
   * This method should be called once per game loop.
   */
  override fun run() {
    while (!eventQueue.isEmpty) {
      val event = eventQueue.removeFirst()
      listeners.forEach { it.onEvent(event) }
    }
  }
}
