package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.engine.common.GameLogger

/** A utility class for shapes. */
object ShapeUtils {

  const val TAG = "ShapeUtils"

  /**
   * Checks if the given [GameRectangle] overlaps the given [GameLine]. Any intersections are added
   * to the provided [intersections] array.
   *
   * @param rectangle The rectangle to check
   * @param line The line to check
   * @param intersections The array to add any intersections to
   * @return True if the given rectangle overlaps the given line, false otherwise
   */
  fun intersectRectangleAndLine(
      rectangle: GameRectangle,
      line: GameLine,
      intersections: Array<Vector2>
  ): Boolean {
    val (lineWorldPoint1, lineWorldPoint2) = line.getWorldPoints()
    GameLogger.debug(TAG, "intersectRectangleAndLine: line = $lineWorldPoint1 and $lineWorldPoint2")

    val lines = rectangle.getAsLines()
    GameLogger.debug(TAG, "intersectRectangleAndLine: rect as lines = $lines")

    var intersects = false
    for (element in lines) {
      val intersection = Vector2()
      val (_worldPoint1, _worldPoint2) = element.getWorldPoints()

      if (Intersector.intersectSegments(
          _worldPoint1, _worldPoint2, lineWorldPoint1, lineWorldPoint2, intersection)) {
        intersects = true
        intersections.add(intersection)
      }
    }

    return intersects
  }
}
