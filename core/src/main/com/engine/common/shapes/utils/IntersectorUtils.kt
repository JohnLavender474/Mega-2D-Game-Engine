package com.engine.common.shapes.utils

import com.badlogic.gdx.math.*
import com.engine.common.shapes.KLine
import com.engine.common.shapes.extensions.CircleExtensions.overlaps
import com.engine.common.shapes.extensions.PolylineExtensions.overlaps
import com.engine.common.shapes.extensions.RectangleExtensions.getAsLines
import com.engine.common.shapes.extensions.RectangleExtensions.overlaps
import kotlin.reflect.KClass

object IntersectorUtils {

    fun overlaps(s1: Shape2D, s2: Shape2D) =
            if (maskShapeTypes(s1, s2, Rectangle::class)) {
                (s1 as Rectangle).overlaps(s2 as Rectangle)
            } else if (maskShapeTypes(s1, s2, Circle::class)) {
                (s1 as Circle).overlaps(s2 as Circle)
            } else if (maskShapeTypes(s1, s2, Polyline::class)) {
                (s1 as Polyline).overlaps(s2 as Polyline)
            } else {
                maskShapeTypes<Rectangle, Circle>(s1, s2)?.let {
                    (s1 as Rectangle).overlaps(s2 as Circle)
                } ?: maskShapeTypes<Rectangle, Polyline>(s1, s2)?.let {
                    (s1 as Rectangle).overlaps(s2 as Polyline)
                } ?: maskShapeTypes<Circle, Polyline>(s1, s2)?.let {
                    (s1 as Circle).overlaps(s2 as Polyline)
                } ?: false
            }


    fun maskShapeTypes(s1: Shape2D, s2: Shape2D, c: KClass<out Shape2D>) = c.isInstance(s1) && c.isInstance(s2)

    inline fun <reified A : Shape2D, reified B : Shape2D> maskShapeTypes(
            s1: Shape2D,
            s2: Shape2D
    ): Pair<A, B>? {
        var p: Pair<A, B>? = null
        if (A::class.isInstance(s1) && B::class.isInstance(s2)) {
            p = Pair(s1 as A, s2 as B)
        } else if (A::class.isInstance(s2) && B::class.isInstance(s1)) {
            p = Pair(s2 as A, s1 as B)
        }
        return p
    }

    fun intersectLineRectangle(p: Polyline, r: Rectangle, intersections: ArrayList<Vector2>) =
            intersectLineRectangle(p.transformedVertices.toTypedArray(), r, intersections)

    fun intersectLineRectangle(v: Array<Float>, r: Rectangle, intersections: ArrayList<Vector2>) =
            intersectLineRectangle(KLine(v), r, intersections)

    fun intersectLineRectangle(line: KLine, r: Rectangle, intersections: ArrayList<Vector2>): Boolean {
        var isIntersection = false
        r.getAsLines().forEach {
            val intersection = Vector2()
            if (intersectLines(it, line, intersection)) {
                isIntersection = true
                intersections.add(intersection)
            }
        }
        return isIntersection
    }

    fun intersectLines(l1: KLine, l2: KLine, intersection: Vector2? = null) =
            Intersector.intersectSegments(l1.point1, l1.point2, l2.point1, l2.point2, intersection)
}
