package com.engine.common.shapes

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import kotlin.math.max

data class KLine(val point1: Vector2, val point2: Vector2): KShape2D {

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

    override fun overlaps(other: KShape2D): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBoundingRectangle(): Rectangle {
        TODO("Not yet implemented")
    }

    override fun positionOnPoint(point: Vector2, position: Position) {
        TODO("Not yet implemented")
    }

    override fun getPositionPoint(position: Position): Vector2 {
        TODO("Not yet implemented")
    }

    override fun contains(point: Vector2?): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(x: Float, y: Float): Boolean {
        TODO("Not yet implemented")
    }

}
