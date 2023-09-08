package com.engine.events

import com.engine.common.objects.Properties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.*

class EventsManagerTest :
    DescribeSpec({
      describe("EventManager") {
        var eventHandled = false
        lateinit var eventsManager: EventsManager

        beforeEach {
          eventHandled = false
          eventsManager = EventsManager()
        }

        it("should queue an event") {
          // if
          val event = Event("testEvent", Properties())

          // when
          eventsManager.queueEvent(event)

          // then
          eventsManager.eventQueue shouldContain event
        }

        it("should add and remove a listener") {
          // if
          val listener = EventListener { eventHandled = true }

          // when
          eventsManager.addListener(listener)

          // then
          eventsManager.listeners.size shouldBe 1

          // when
          eventsManager.removeListener(listener)

          // then
          eventsManager.listeners.shouldBeEmpty()
        }

        it("should clear all listeners") {
          // if
          val listener1 = EventListener {}
          val listener2 = EventListener {}

          // when
          eventsManager.addListener(listener1)
          eventsManager.addListener(listener2)
          eventsManager.clearListeners()

          // then
          eventsManager.listeners.shouldBeEmpty()
        }

        it("should run and handle an event") {
          // if
          val listener = EventListener { eventHandled = true }
          val event = Event("testEvent", Properties())

          // when
          eventsManager.addListener(listener)
          eventsManager.queueEvent(event)
          eventsManager.run()

          // then
          eventHandled shouldBe true
          eventsManager.eventQueue.shouldBeEmpty()
        }

        it("should handle multiple listeners") {
          // if
          val listener1 = mockk<EventListener> { every { onEvent(any()) } just Runs }
          val listener2 = mockk<EventListener> { every { onEvent(any()) } just Runs }

          // when
          eventsManager.addListener(listener1)
          eventsManager.addListener(listener2)
          val event = Event("testEvent", Properties())
          eventsManager.queueEvent(event)
          eventsManager.run()

          // then
          verify(exactly = 1) {
            listener1.onEvent(any())
            listener2.onEvent(any())
          }
        }
      }
    })
