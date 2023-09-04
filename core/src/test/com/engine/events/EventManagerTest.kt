package com.engine.events

import com.engine.common.objects.Properties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.*

class EventManagerTest :
    DescribeSpec({
      describe("EventManager") {
        var eventHandled = false
        lateinit var eventManager: EventManager

        beforeEach {
          eventHandled = false
          eventManager = EventManager()
        }

        it("should queue an event") {
          // if
          val event = Event("testEvent", Properties())

          // when
          eventManager.queueEvent(event)

          // then
          eventManager.eventQueue shouldContain event
        }

        it("should add and remove a listener") {
          // if
          val listener = EventListener { eventHandled = true }

          // when
          eventManager.addListener(listener)

          // then
          eventManager.listeners.size shouldBe 1

          // when
          eventManager.removeListener(listener)

          // then
          eventManager.listeners.shouldBeEmpty()
        }

        it("should clear all listeners") {
          // if
          val listener1 = EventListener {}
          val listener2 = EventListener {}

          // when
          eventManager.addListener(listener1)
          eventManager.addListener(listener2)
          eventManager.clearListeners()

          // then
          eventManager.listeners.shouldBeEmpty()
        }

        it("should run and handle an event") {
          // if
          val listener = EventListener { eventHandled = true }
          val event = Event("testEvent", Properties())

          // when
          eventManager.addListener(listener)
          eventManager.queueEvent(event)
          eventManager.run()

          // then
          eventHandled shouldBe true
          eventManager.eventQueue.shouldBeEmpty()
        }

        it("should handle multiple listeners") {
          // if
          val listener1 = mockk<EventListener> { every { onEvent(any()) } just Runs }
          val listener2 = mockk<EventListener> { every { onEvent(any()) } just Runs }

          // when
          eventManager.addListener(listener1)
          eventManager.addListener(listener2)
          val event = Event("testEvent", Properties())
          eventManager.queueEvent(event)
          eventManager.run()

          // then
          verify(exactly = 1) {
            listener1.onEvent(any())
            listener2.onEvent(any())
          }
        }
      }
    })
