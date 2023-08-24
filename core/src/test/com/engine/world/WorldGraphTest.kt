package com.engine.world

import com.engine.common.objects.Dimensions
import com.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain

class WorldGraphSpec :
    DescribeSpec({
      describe("WorldGraph class") {
        val worldGraph = WorldGraph(100, 100, 10)

        it("should add bodies correctly") {
          val body = Body(5f, 5f, BodyType.ABSTRACT)
          worldGraph.addBody(body)

          var bodiesAtPosition = worldGraph.getBodies(0, 0)
          bodiesAtPosition shouldContain body

          bodiesAtPosition = worldGraph.getBodies(0, 1)
          bodiesAtPosition shouldNotContain body

          bodiesAtPosition = worldGraph.getBodies(1, 0)
          bodiesAtPosition shouldNotContain body
        }

        it("should add fixtures correctly") {
          val fixture = Fixture(/* construct your Fixture object */ )
          worldGraph.addFixture(fixture)

          val fixturesAtPosition = worldGraph.getFixtures(0, 0)
          fixturesAtPosition shouldContain fixture
        }

        it("should retrieve fixtures overlapping correctly") {
          val fixture = Fixture(/* construct your Fixture object */ )
          worldGraph.addFixture(fixture)

          val overlappingFixtures = worldGraph.getFixturesOverlapping(fixture)
          overlappingFixtures shouldContain fixture
        }

        it("should retrieve bodies overlapping correctly") {
          val body = Body(/* construct your Body object */ )
          worldGraph.addBody(body)

          val overlappingBodies = worldGraph.getBodiesOverlapping(body)
          overlappingBodies shouldContain body
        }

        it("should reset correctly") {
          val body = Body(/* construct your Body object */ )
          val fixture = Fixture(/* construct your Fixture object */ )
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
