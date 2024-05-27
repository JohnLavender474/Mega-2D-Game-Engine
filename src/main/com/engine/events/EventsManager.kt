package com.engine.events

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.badlogic.gdx.utils.OrderedSet
import com.engine.common.GameLogger
import com.engine.common.extensions.putIfAbsentAndGet
import java.util.*
import java.util.function.Predicate

/**
 * A manager for [Event]s and [IEventListener]s. [Event]s are submitted to the [EventsManager] and
 * [IEventListener]s are added to the [EventsManager]. When an [Event] is submitted, all
 * [IEventListener]s are notified of the [Event] when the [run] method is called.
 */
class EventsManager(
    private val debugEventFilter: (Event) -> Boolean = { true },
    private val debugListenerFilter: (IEventListener) -> Boolean = { true }
) : IEventsManager {

    companion object {
        const val TAG = "EventsManager"
    }

    internal val listeners = OrderedSet<IEventListener>()
    internal val listenersToAdd = LinkedList<IEventListener>()
    internal val listenersToRemove = LinkedList<IEventListener>()

    internal val events = OrderedMap<Any, Array<Event>>()
    internal val eventsToAdd = LinkedList<Event>()

    internal var running = false
        private set

    private var setToClearListeners = false

    /**
     * Creates an [EventsManager] with the given [debugEventFilter] and [debugListenerFilter].
     *
     * @param debugEventFilter The filter for [Event]s to debug.
     * @param debugListenerFilter The filter for [IEventListener]s to debug.
     */
    constructor(
        debugEventFilter: Predicate<Event> = Predicate<Event> { true },
        debugListenerFilter: Predicate<IEventListener> = Predicate<IEventListener> { true }
    ) : this(
        { debugEventFilter.test(it) },
        { debugListenerFilter.test(it) }
    )

    /**
     * Submits an [Event] to this [EventsManager]. If the [EventsManager] is not running, the [Event]
     * will be added immediately. If the [EventsManager] is running, the [Event] will be added in the
     * next update cycle.
     *
     * @param event The [Event] to submit.
     */
    override fun submitEvent(event: Event) {
        if (running) {
            if (debugEventFilter.invoke(event)) GameLogger.debug(
                TAG, "submitEvent(): Queuing event to be added in next update cycle: $event"
            )
            eventsToAdd.add(event)
        } else submitEventNow(event)
    }

    private fun submitEventNow(event: Event) {
        if (debugEventFilter.invoke(event)) GameLogger.debug(TAG, "submitEvent(): Adding event now: $event")

        val eventKey = event.key
        events.putIfAbsentAndGet(eventKey, Array()).add(event)

        if (debugEventFilter.invoke(event)) GameLogger.debug(
            TAG, "submitEvent(): Events map entry after adding event: $events"
        )
    }

    /**
     * Adds an [IEventListener] to this [EventsManager]. The [IEventListener] will be notified of
     * [Event]s when the [run] method is called. If the [EventsManager] is not running, the
     * [IEventListener] will be added immediately. If the [EventsManager] is running, the
     * [IEventListener] will be added in the next update cycle.
     *
     * @param listener The [IEventListener] to add.
     * @return If the [IEventListener] was added.
     */
    override fun addListener(listener: IEventListener) = if (running) {
        if (debugListenerFilter.invoke(listener)) GameLogger.debug(
            TAG, "addListener(): Queuing listener to be added in next update cycle: $listener"
        )
        listenersToAdd.add(listener)
    } else addListenerNow(listener)

    private fun addListenerNow(listener: IEventListener): Boolean {
        if (debugListenerFilter.invoke(listener)) GameLogger.debug(TAG, "addListener(): Adding listener now: $listener")
        return listeners.add(listener)
    }

    /**
     * Removes an [IEventListener] from this [EventsManager]. The [IEventListener] will no longer be
     * notified of [Event]s when the [run] method is called. If the [EventsManager] is not running,
     * the [IEventListener] will be removed immediately. If the [EventsManager] is running, the
     * [IEventListener] will be removed in the next update cycle.
     *
     * @param listener The [IEventListener] to remove.
     * @return If the [IEventListener] was removed.
     */
    override fun removeListener(listener: IEventListener) = if (running) {
        if (debugListenerFilter.invoke(listener)) GameLogger.debug(
            TAG, "removeListener(): Queuing listener to be removed in next update cycle: $listener"
        )
        listenersToRemove.add(listener)
    } else removeListenerNow(listener)

    private fun removeListenerNow(listener: IEventListener): Boolean {
        if (debugListenerFilter.invoke(listener)) GameLogger.debug(
            TAG, "removeListener(): Removing listener now: $listener"
        )
        return listeners.remove(listener)
    }

    /**
     * Removes all [IEventListener]s from this [EventsManager]. The [IEventListener]s will no longer
     * be notified of [Event]s when the [run] method is called. If the [EventsManager] is not running,
     * the [IEventListener]s will be removed immediately. If the [EventsManager] is running, the
     * [IEventListener]s will be removed in the next update cycle.
     */
    override fun clearListeners() {
        if (running) {
            GameLogger.debug(
                TAG, "clearListeners(): Queuing all listeners to be removed in next update cycle"
            )
            setToClearListeners = true
        } else clearListenersNow()
    }

    private fun clearListenersNow() {
        GameLogger.debug(TAG, "clearListeners(): Clearing all listeners now")
        listeners.clear()
    }

    /**
     * Notifies all [IEventListener]s of [Event]s that have been submitted to this [EventsManager].
     * After this method is finished, all events will be cleared. This method should be called once
     * per game loop.
     */
    override fun run() {
        running = true

        while (!eventsToAdd.isEmpty()) submitEventNow(eventsToAdd.poll())
        while (!listenersToAdd.isEmpty()) addListenerNow(listenersToAdd.poll())
        while (!listenersToRemove.isEmpty()) removeListenerNow(listenersToRemove.poll())
        if (setToClearListeners) {
            clearListenersNow()
            setToClearListeners = false
        }

        listeners.forEach { listener ->
            val eventKeyMask = listener.eventKeyMask
            if (eventKeyMask.isEmpty) {
                if (debugListenerFilter.invoke(listener)) GameLogger.debug(
                    TAG, "run(): Listener has empty event key mask: $listener"
                )

                events.values().flatten().forEach { event ->
                    if (debugListenerFilter.invoke(listener)) GameLogger.debug(
                        TAG, "run(): Notifying listener $listener of event: $event"
                    )

                    listener.onEvent(event)
                }
            } else {
                events.forEach {
                    val eventKey = it.key
                    val eventsArray = it.value
                    if (eventKeyMask.contains(eventKey)) {
                        if (debugListenerFilter.invoke(listener)) GameLogger.debug(
                            TAG, "run(): Notifying listener $listener of event type: $eventKey"
                        )

                        eventsArray.forEach { event ->
                            if (debugListenerFilter.invoke(listener)) GameLogger.debug(
                                TAG, "run(): Notifying listener $listener of event: $event"
                            )

                            listener.onEvent(event)
                        }
                    }
                }
            }
        }
        events.clear()
        running = false
    }
}
