package com.engine.cullables

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CullableOnUncontainedTest :
    DescribeSpec({
      describe("CullableOnUncontained") {
        it("should be culled when not contained") {
          val container = "within"
          var containable = "in"

          val cullableOnUncontained =
              CullableOnUncontained({ container }) { containable in it }

          cullableOnUncontained.shouldBeCulled() shouldBe false

          // Update the container to contain the containable
          containable = "out"
          cullableOnUncontained.shouldBeCulled() shouldBe true

          // Reset the container to not contain the containable
          containable = "in"
          cullableOnUncontained.shouldBeCulled() shouldBe false
        }

        it("should not be culled when contained") {
          val container = "within"
          var containable = "in"

          val cullableOnUncontained = CullableOnUncontained({ container }) { containable in it }

          cullableOnUncontained.shouldBeCulled() shouldBe false

          // Set the container to contain the containable
          containable = "within"
          cullableOnUncontained.shouldBeCulled() shouldBe false
        }
      }
    })
