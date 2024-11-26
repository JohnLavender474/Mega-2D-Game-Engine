package com.mega.game.engine.events

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.badlogic.gdx.utils.OrderedSet
import com.mega.game.engine.common.extensions.putIfAbsentAndGet
import com.mega.game.engine.common.objects.SimpleQueueSet
import java.util.*

/**
 * A manager for [Event]s and [IEventListener]s. [Event]s are submitted to the [EventsManager] and [IEventListener]s
 * are added to the [EventsManager]. When an [Event] is submitted, all [IEventListener]s that are set to listen to
 * the event are notified of the event on the next call to [run].
 */
class EventsManager : Runnable {

    companion object {
        const val TAG = "EventsManager"
    }

    internal val listeners = OrderedSet<IEventListener>()
    internal val listenersToAdd = SimpleQueueSet<IEventListener>()
    internal val listenersToRemove = SimpleQueueSet<IEventListener>()

    internal val events = OrderedMap<Any, Array<Event>>()
    internal val eventsToAdd = LinkedList<Event>()

    internal var running = false
        private set

    private var setToClearListeners = false

    /**
     * Submits an [Event] to this [EventsManager]. If the [EventsManager] is not running, the [Event]
     * will be added immediately. If the [EventsManager] is running, the [Event] will be added in the
     * next update cycle.
     *
     * @param event The [Event] to submit.
     */
    fun submitEvent(event: Event) {
        if (running) eventsToAdd.add(event) else submitEventNow(event)
    }

    private fun submitEventNow(event: Event) {
        val eventKey = event.key
        events.putIfAbsentAndGet(eventKey, Array()).add(event)
    }

    /**
     * Returns true if the [listener] is registered to this events manager.
     *
     * @param listener the listener
     * @return if the listener is registered
     */
    fun isListener(listener: IEventListener) = listeners.contains(listener)

    /**
     * Returns true if the [listener] is queued to be registered to this events manager.
     *
     * @param listener the listener
     * @return if the listener is queued to be registered
     */
    fun isQueuedToBeAdded(listener: IEventListener) = listenersToAdd.contains(listener)

    /**
     * Returns true if the [listener] is queued to be removed from this events manager.
     *
     * @param listener the listener
     * @return if the listener is queued to be removed
     */
    fun isQueuedToBeRemoved(listener: IEventListener) = listenersToRemove.contains(listener)

    /**
     * Adds an [IEventListener] to this [EventsManager]. The [IEventListener] will be notified of
     * [Event]s when the [run] method is called. If the [EventsManager] is not running, the
     * [IEventListener] will be added immediately. If the [EventsManager] is running, the
     * [IEventListener] will be added in the next update cycle.
     *
     * @param listener The [IEventListener] to add.
     * @return If the [IEventListener] was added.
     */
    fun addListener(listener: IEventListener) =
        if (running) listenersToAdd.add(listener) else addListenerNow(listener)

    private fun addListenerNow(listener: IEventListener) = listeners.add(listener)

    /**
     * Removes an [IEventListener] from this [EventsManager]. The [IEventListener] will no longer be
     * notified of [Event]s when the [run] method is called. If the [EventsManager] is not running,
     * the [IEventListener] will be removed immediately. If the [EventsManager] is running, the
     * [IEventListener] will be removed in the next update cycle.
     *
     * @param listener The [IEventListener] to remove.
     * @return If the [IEventListener] was removed.
     */
    fun removeListener(listener: IEventListener) =
        if (running) listenersToRemove.add(listener) else removeListenerNow(listener)

    private fun removeListenerNow(listener: IEventListener): Boolean = listeners.remove(listener)

    /**
     * Removes all [IEventListener]s from this [EventsManager]. The [IEventListener]s will no longer
     * be notified of [Event]s when the [run] method is called. If the [EventsManager] is not running,
     * the [IEventListener]s will be removed immediately. If the [EventsManager] is running, the
     * [IEventListener]s will be removed in the next update cycle.
     */
    fun clearListeners() = if (running) setToClearListeners = true else clearListenersNow()

    private fun clearListenersNow() = listeners.clear()

    /**
     * Notifies all [IEventListener]s of [Event]s that have been submitted to this [EventsManager].
     * After this method is finished, all events will be cleared. This method should be called once
     * per game loop.
     */
    override fun run() {
        running = true

        while (!eventsToAdd.isEmpty()) submitEventNow(eventsToAdd.poll())
        while (!listenersToAdd.isEmpty()) addListenerNow(listenersToAdd.remove())
        while (!listenersToRemove.isEmpty()) removeListenerNow(listenersToRemove.remove())
        if (setToClearListeners) {
            clearListenersNow()
            setToClearListeners = false
        }

        listeners.forEach { listener ->
            val eventKeyMask = listener.eventKeyMask
            events.forEach {
                val eventKey = it.key
                val eventsArray = it.value
                if (eventKeyMask.contains(eventKey)) eventsArray.forEach { event -> listener.onEvent(event) }
            }
        }
        events.clear()
        running = false
    }
}
