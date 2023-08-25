package com.engine.world

import com.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ContactDescribeSpec :
    DescribeSpec({
      describe("Contact data class") {
        val fixture1 = Fixture(GameRectangle(), "type1")
        val fixture2 = Fixture(GameRectangle(), "type2")
        val contact = Contact(fixture1, fixture2)

        it("should have the correct initial properties") {
          contact.fixture1 shouldBe fixture1
          contact.fixture2 shouldBe fixture2
        }

        it("should get fixtures in order correctly") {
          val fixtureType1 = "type1"
          val fixtureType2 = "type2"

          var fixturesInOrder = contact.getFixturesInOrder(fixtureType1, fixtureType2)
          fixturesInOrder shouldBe Pair(fixture1, fixture2)

          fixturesInOrder = contact.getFixturesInOrder(fixtureType2, fixtureType1)
          fixturesInOrder shouldBe Pair(fixture2, fixture1)
        }

        it("should check if fixtures are of types correctly") {
          val fixtureType1 = "type1"
          val fixtureType2 = "type2"
          val badFixtureType = "type3"

          val test: (String, String, Boolean) -> Unit = { f1, f2, expected ->
            contact.fixturesAreOfTypes(f1, f2) shouldBe expected
          }

          test(fixtureType1, fixtureType2, true)
          test(fixtureType2, fixtureType1, true)
          test(fixtureType1, fixtureType1, false)
          test(fixtureType2, fixtureType2, false)
          test(fixtureType1, badFixtureType, false)
          test(fixtureType2, badFixtureType, false)
          test(badFixtureType, badFixtureType, false)
        }

        it("should check equality correctly") {
          val sameContact = Contact(fixture1, fixture2)
          val swappedContact = Contact(fixture2, fixture1)
          val differentFixture = Fixture(GameRectangle(), "type3")
          val differentContact = Contact(fixture1, differentFixture)

          (contact == sameContact) shouldBe true
          (contact == swappedContact) shouldBe true
          (contact == differentContact) shouldBe false
        }

        it("should have the correct hash code") {
          val expectedHashCode = 49 + 7 * fixture1.hashCode() + 7 * fixture2.hashCode()
          contact.hashCode() shouldBe expectedHashCode
        }
      }
    })
