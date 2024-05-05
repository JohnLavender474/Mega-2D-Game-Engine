package com.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2

/**
 * Utility class for shapes.
 */
object ShapeUtils {

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
            if (i == 0 && Intersector.intersectSegmentCircle(
                    Vector2(vertices[vertices.size - 2], vertices[vertices.size - 1]),
                    Vector2(vertices[0], vertices[1]),
                    center,
                    squareRadius
                )
            ) return true
            else if (Intersector.intersectSegmentCircle(
                    Vector2(vertices[i - 2], vertices[i - 1]),
                    Vector2(vertices[i], vertices[i + 1]),
                    center,
                    squareRadius
                )
            ) return true
        }
        return polygon.contains(circle.x, circle.y);
    }
}