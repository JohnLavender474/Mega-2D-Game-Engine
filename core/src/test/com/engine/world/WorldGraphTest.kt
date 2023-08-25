package com.engine.world

import com.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain

class WorldGraphTest :
    DescribeSpec({
      describe("WorldGraph") {
        val worldGraph = WorldGraph(100, 100, 10)

        beforeEach { worldGraph.reset() }

        it("should add bodies correctly") {
          val body = Body(BodyType.ABSTRACT, 0f, 0f, 5f, 5f)
          worldGraph.addBody(body)

          var bodiesAtPosition = worldGraph.getBodies(0, 0)
          bodiesAtPosition shouldContain body

          bodiesAtPosition = worldGraph.getBodies(0, 1)
          bodiesAtPosition shouldNotContain body

          bodiesAtPosition = worldGraph.getBodies(1, 0)
          bodiesAtPosition shouldNotContain body
        }

        it("should add fixtures correctly") {
          val fixture = Fixture(GameRectangle(0f, 0f, 5f, 5f), "test")
          worldGraph.addFixture(fixture)

          var fixturesAtPosition = worldGraph.getFixtures(0, 0)
          fixturesAtPosition shouldContain fixture

          fixturesAtPosition = worldGraph.getFixtures(0, 1)
          fixturesAtPosition shouldNotContain fixture

          fixturesAtPosition = worldGraph.getFixtures(1, 0)
          fixturesAtPosition shouldNotContain fixture
        }

        it("should retrieve fixtures overlapping correctly") {
          val fixture1 = Fixture(GameRectangle(0f, 0f, 5f, 5f), "test")
          val fixture2 = Fixture(GameRectangle(2.5f, 2.5f, 7.5f, 7.5f), "test")
          worldGraph.addFixture(fixture1)
          worldGraph.addFixture(fixture2)

          val overlappingFixtures = worldGraph.getFixturesOverlapping(fixture1)
          overlappingFixtures shouldContain fixture2
        }

        it("should retrieve bodies overlapping correctly") {
          val body1 = Body(BodyType.ABSTRACT, 0f, 0f, 5f, 5f)
          val body2 = Body(BodyType.ABSTRACT, 2.5f, 2.5f, 7.5f, 7.5f)
          worldGraph.addBody(body1)
          worldGraph.addBody(body2)

          val overlappingBodies = worldGraph.getBodiesOverlapping(body1)
          overlappingBodies shouldContain body2
        }

        it("should reset correctly") {
          val body = Body(BodyType.ABSTRACT, 0f, 0f, 5f, 5f)
          val fixture = Fixture(GameRectangle(0f, 0f, 5f, 5f), "test")
          worldGraph.addBody(body)
          worldGraph.addFixture(fixture)

          worldGraph.reset()

          val bodiesAtPosition = worldGraph.getBodies(0, 0)
          val fixturesAtPosition = worldGraph.getFixtures(0, 0)

          bodiesAtPosition.shouldBeEmpty()
          fixturesAtPosition.shouldBeEmpty()
        }
      }
    })
