package com.engine.cullables

import com.engine.common.objects.props
import com.engine.events.Event
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CullableOnEventTest :
    DescribeSpec({
      describe("CullableOnEvent") {
        it("should be culled on specified event") {
          val eventKey = "testEvent"
          val eventProperties = props("property1" to 42, "property2" to "test")
          val event = Event(eventKey, eventProperties)

          // Create a CullableOnEvent that should be culled on the specified event
          val cullableOnEvent = CullableOnEvent { it.eventKey == eventKey }

          cullableOnEvent.shouldBeCulled() shouldBe false
          cullableOnEvent.onEvent(event)
          cullableOnEvent.shouldBeCulled() shouldBe true

          // Reset the CullableOnEvent and test again
          cullableOnEvent.reset()

          cullableOnEvent.shouldBeCulled() shouldBe false
          cullableOnEvent.onEvent(event)
          cullableOnEvent.shouldBeCulled() shouldBe true
        }

        it("should not be culled on different event") {
          val eventKey = "testEvent"
          val eventProperties = props("property1" to 42, "property2" to "test")
          val event = Event(eventKey, eventProperties)

          // Create a CullableOnEvent that should not be culled on a different event
          val cullableOnEvent = CullableOnEvent { it.eventKey == "differentEvent" }

          cullableOnEvent.shouldBeCulled() shouldBe false
          cullableOnEvent.onEvent(event)
          cullableOnEvent.shouldBeCulled() shouldBe false
        }

        it("should reset cullable state") {
          val eventKey = "testEvent"
          val eventProperties = props("property1" to 42, "property2" to "test")
          val event = Event(eventKey, eventProperties)

          val cullableOnEvent = CullableOnEvent { it.eventKey == eventKey }

          cullableOnEvent.onEvent(event)
          cullableOnEvent.shouldBeCulled() shouldBe true

          cullableOnEvent.reset()
          cullableOnEvent.shouldBeCulled() shouldBe false
        }
      }
    })
