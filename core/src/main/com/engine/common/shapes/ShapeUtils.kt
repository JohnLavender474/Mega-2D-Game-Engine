package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

/** A utility class for shapes. */
object ShapeUtils {

  /**
   * Checks if the given [GameRectangle] overlaps the given [GameLine]. Any intersections are added
   * to the provided [intersections] array.
   *
   * @param rectangle The rectangle to check
   * @param line The line to check
   * @return True if the given rectangle overlaps the given line, false otherwise
   */
  fun intersectRectangleAndLine(
      rectangle: GameRectangle,
      line: GameLine,
      intersections: Array<Vector2>
  ): Boolean {
    var intersects = false
    val lines = rectangle.getAsLines()
    for (element in lines) {
      val intersection = Vector2()
      if (Intersector.intersectSegments(
          element.point1, element.point2, line.point1, line.point2, intersection)) {
        intersects = true
        intersections.add(intersection)
      }
    }
    return intersects
  }
}
