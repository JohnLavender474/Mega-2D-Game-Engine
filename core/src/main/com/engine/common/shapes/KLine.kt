package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.KPolyline
import com.engine.common.shapes.KRectangle
import com.engine.common.enums.Position
import com.engine.common.shapes.extensions.PolylineExtensions.getAsLines
import kotlin.math.max
import kotlin.math.min

data class KLine(val point1: Vector2, val point2: Vector2) : KShape2D {

    constructor(x1: Float, y1: Float, x2: Float, y2:Float) : this(Vector2(x1, y1), Vector2(x2, y2))

    constructor(v: Array<Float>) : this(Vector2(v[0], v[1]), Vector2(v[2], v[3]))

    override fun getCenter() = Vector2((point1.x + point2.x) / 2f, (point1.y + point2.y) / 2f)

    override fun setCenterX(centerX: Float) {
        val currCenterX = getCenter().x
        val translateX = centerX - currCenterX
        translate(translateX, 0f)
    }

    override fun setCenterY(centerY: Float) {
        val currCenterY = getCenter().y
        val translateY = centerY - currCenterY
        translate(0f, translateY)
    }

    override fun getMaxX() = max(point1.x, point2.x)

    override fun getMaxY() = max(point1.y, point2.y)

    override fun setMaxX(maxX: Float) {
        val currMaxX = getMaxX()
        val translateX = maxX - currMaxX
        translate(translateX, 0f)
    }

    override fun setMaxY(maxY: Float) {
        val currMaxY = getMaxY()
        val translateY = maxY - currMaxY
        translate(0f, translateY)
    }

    override fun translate(translateX: Float, translateY: Float) {
        point1.x += translateX
        point2.x += translateX
        point1.y += translateY
        point2.y += translateY
    }

    override fun overlaps(other: KShape2D) = when (other) {
        is KRectangle -> Intersector.intersectSegmentRectangle(point1, point2, other)
        is KCircle -> Intersector.intersectSegmentCircle(point1, point2, other.getCenter(), other.radius)
        is KLine -> Intersector.intersectLines(point1, point2, other.point1, other.point2, Vector2())
        is KPolyline -> other.getAsLines().any {
            Intersector.intersectLines(it.point1, it.point2, point1, point2, Vector2())
        }
        else -> false
    }

    override fun getBoundingRectangle(): Rectangle {
        val minX = min(point1.x, point2.x)
        val maxX = max(point1.x, point2.x)
        val minY = min(point1.y, point2.y)
        val maxY = max(point1.y, point2.y)
        return Rectangle(minX, minY, maxX, maxY)
    }

    override fun positionOnPoint(point: Vector2, position: Position) {
        TODO("Not yet implemented")
    }

    override fun getPositionPoint(position: Position): Vector2 {
        TODO("Not yet implemented")
    }

    override fun contains(point: Vector2) = Intersector.pointLineSide(point1, point2, point) == 0

    override fun contains(x: Float, y: Float) = contains(Vector2(x, y))

}
