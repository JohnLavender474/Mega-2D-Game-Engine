package com.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.extensions.PolylineExtensions.getAsLines
import kotlin.math.pow

data class KCircle(val x: Float, val y: Float, val radius: Float) : Circle(x, y, radius), KShape2D {

    constructor() : this(0f, 0f, 0f)

    constructor(position: Vector2, radius: Float) : this(position.x, position.y, radius)

    constructor(circle: Circle) : this(circle.x, circle.y, circle.radius)

    constructor(center: Vector2, edge: Vector2) : this(center.x, center.y, Vector2.len(center.x - edge.x, center.y - edge.y))

    override fun getCenter(): Vector2 = Vector2(x, y)

    override fun setCenterX(centerX: Float) {
        x = centerX
    }

    override fun setCenterY(centerY: Float) {
        y = centerY
    }

    override fun getMaxX() = x + radius

    override fun getMaxY() = y + radius

    override fun setMaxX(maxX: Float) {
        x = maxX - radius
    }

    override fun setMaxY(maxY: Float) {
        y = maxY - radius
    }

    override fun translate(translateX: Float, translateY: Float) {
        x += translateX
        y += translateY
    }

    override fun overlaps(other: KShape2D) = when (other) {
        is KRectangle -> Intersector.overlaps(this, other)
        is KCircle -> Intersector.overlaps(this, other)
        is KLine -> Intersector.intersectSegmentCircle(other.point1, other.point2, getCenter(), radius.pow(2))
        is KPolyline -> other.getAsLines().any {
            Intersector.intersectSegmentCircle(it.point1, it.point2, getCenter(), radius.pow(2))
        }
        else -> false
    }

    override fun getBoundingRectangle(): Rectangle = Rectangle().setSize(radius * 2).setCenter(x, y)

    override fun positionOnPoint(point: Vector2, position: Position) {
        when (position) {
            Position.TOP_LEFT -> {
                x = point.x + radius
                y = point.y - radius
            }
            Position.TOP_CENTER -> {
                x = point.x
                y = point.y - radius
            }
            Position.TOP_RIGHT -> {
                x = point.x - radius
                y = point.y - radius
            }
            Position.CENTER_LEFT -> {
                x = point.x + radius
                y = point.y
            }
            Position.CENTER -> {
                x = point.x
                y = point.y
            }
            Position.CENTER_RIGHT -> {
                x = point.x - radius
                y = point.y
            }
            Position.BOTTOM_LEFT -> {
                x = point.x + radius
                y = point.y + radius
            }
            Position.BOTTOM_CENTER -> {
                x = point.x
                y = point.y + radius
            }
            Position.BOTTOM_RIGHT -> {
                x = point.x - radius
                y = point.y + radius
            }
        }
    }

    override fun getPositionPoint(position: Position) = when (position) {
        Position.TOP_LEFT -> Vector2(x - radius, y + radius)
        Position.TOP_CENTER -> Vector2(x, y + radius)
        Position.TOP_RIGHT -> Vector2(x + radius, y + radius)
        Position.CENTER_LEFT -> Vector2(x - radius, y)
        Position.CENTER -> Vector2(x, y)
        Position.CENTER_RIGHT -> Vector2(x + radius, y)
        Position.BOTTOM_LEFT -> Vector2(x - radius, y - radius)
        Position.BOTTOM_CENTER -> Vector2(x, y - radius)
        Position.BOTTOM_RIGHT -> Vector2(x + radius, y - radius)
    }
}