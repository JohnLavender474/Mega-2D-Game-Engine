package com.engine.pathfinding

import com.badlogic.gdx.math.Vector2
import com.engine.common.objects.pairTo
import com.engine.common.shapes.GameRectangle
import com.engine.graph.GraphMap
import com.engine.graph.convertToGraphCoordinate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class PathfinderTest :
    DescribeSpec({
      val worldGraph = mockk<GraphMap>()
      val params = createTestPathfinderParams()

      describe("Pathfinder class") {
        beforeEach {
          // Reset the mocks before each test
          clearMocks(worldGraph)
        }

        it("should find a valid path") {
          // Set up a valid path in the worldGraph
          every { worldGraph.convertToGraphCoordinate(any()) } answers
              {
                if (firstArg<Vector2>() == params.startSupplier()) 0 pairTo 0 else 2 pairTo 2
              }

          // Create a Pathfinder instance
          val pathfinder = Pathfinder(worldGraph, params)

          // Test finding a valid path
          val path = pathfinder.call()

          // Assert that the path is not null (a valid path is found)
          path shouldNotBe null

          // You can add more assertions to validate the path if needed
          // For example, check if the path contains the expected coordinates.
        }

        it("should not find a path when none exists") {
          // Set up a scenario where there is no valid path in the worldGraph
          every { worldGraph.convertToGraphCoordinate(any()) } answers
              {
                if (firstArg<Vector2>() == params.startSupplier()) 0 pairTo 0 else 10 pairTo 10
              }

          // Create a Pathfinder instance
          val pathfinder = Pathfinder(worldGraph, params)

          // Test finding a path when there's no valid path
          val path = pathfinder.call()

          // Assert that the path is null (no valid path is found)
          path shouldBe null
        }
      }
    })

// Helper method to create test PathfinderParams
private fun createTestPathfinderParams(): PathfinderParams {
  return PathfinderParams(
      { Vector2(0f, 0f) }, // Replace with your start point supplier logic
      { Vector2(2f, 2f) }, // Replace with your target point supplier logic
      { obj -> obj is GameRectangle }, // Replace with your filter logic
      true, // Allow diagonal movement
      { true } // Replace with your target listener logic
      )
}
