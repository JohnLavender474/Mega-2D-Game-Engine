package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.KLine
import com.engine.common.shapes.extensions.RectangleExtensions.overlaps
import com.engine.common.shapes.utils.IntersectorUtils

object PolylineExtensions {

    fun Polyline.overlaps(r: Rectangle) = r.overlaps(this)

    fun Polyline.overlaps(p: Polyline) =
            getAsLines().any { l1 -> p.getAsLines().any { l2 -> IntersectorUtils.intersectLines(l1, l2) } }

    fun Polyline.overlaps(c: Circle) =
            getAsLines().any { Intersector.intersectSegmentCircle(it.point1, it.point2, c, null) }

    fun Polyline.getAsLines(): List<KLine> {
        val v = transformedVertices
        val lines = ArrayList<KLine>()
        for (i in 0..v.size step 4) {
            val p1 = Vector2(v[i], v[i + 1])
            val p2 = Vector2(v[i + 2], v[i + 3])
            lines.add(KLine(p1, p2))
        }
        return lines
    }

}