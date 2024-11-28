package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array


object ShapeUtils {

    
    fun intersectRectangleAndLine(
        rectangle: GameRectangle,
        line: GameLine,
        intersections: Array<Vector2>
    ): Boolean {
        val (lineWorldPoint1, lineWorldPoint2) = line.calculateWorldPoints()
        val lines = rectangle.getAsLines()
        var intersects = false
        for (element in lines) {
            val intersection = Vector2()
            val (_worldPoint1, _worldPoint2) = element.calculateWorldPoints()
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