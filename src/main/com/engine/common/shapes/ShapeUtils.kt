package com.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

/**
 * Utility class for shapes.
 */
object ShapeUtils {

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
        val lines = rectangle.getAsLines()
        var intersects = false
        for (element in lines) {
            val intersection = Vector2()
            val (_worldPoint1, _worldPoint2) = element.getWorldPoints()
            if (Intersector.intersectSegments(
                    _worldPoint1, _worldPoint2, lineWorldPoint1, lineWorldPoint2, intersection
                )
            ) {
                intersects = true
                intersections.add(intersection)
            }
        }
        return intersects
    }

    /**
     * Determines if a circle overlaps a polygon.
     *
     * @param circle The circle to check for overlap.
     * @param polygon The polygon to check for overlap.
     * @return True if the circle overlaps the polygon, false otherwise.
     */
    fun overlaps(circle: Circle, polygon: Polygon): Boolean {
        val vertices = polygon.transformedVertices
        val center = Vector2(circle.x, circle.y)
        val squareRadius = circle.radius * circle.radius
        for (i in vertices.indices step 2) {
            if (i == 0) {
                if (Intersector.intersectSegmentCircle(
                        Vector2(vertices[vertices.size - 2], vertices[vertices.size - 1]),
                        Vector2(vertices[0], vertices[1]),
                        center,
                        squareRadius
                    )
                ) return true
            } else if (Intersector.intersectSegmentCircle(
                    Vector2(vertices[i - 2], vertices[i - 1]),
                    Vector2(vertices[i], vertices[i + 1]),
                    center,
                    squareRadius
                )
            ) return true
        }
        return polygon.contains(circle.x, circle.y)
    }
}