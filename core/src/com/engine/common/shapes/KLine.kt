package com.engine.common.shapes

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

data class KLine(val point1: Vector2, val point2: Vector2): KShape2D {

    constructor(v: Array<Float>) : this(Vector2(v[0], v[1]), Vector2(v[2], v[3]))

    override fun getCenter(): Vector2 {
        TODO("Not yet implemented")
    }

    override fun setCenterX(centerX: Float) {
        TODO("Not yet implemented")
    }

    override fun setCenterY(centerY: Float) {
        TODO("Not yet implemented")
    }

    override fun getMaxX(): Float {
        TODO("Not yet implemented")
    }

    override fun getMaxY(): Float {
        TODO("Not yet implemented")
    }

    override fun setMaxX(maxX: Float) {
        TODO("Not yet implemented")
    }

    override fun setMaxY(maxY: Float) {
        TODO("Not yet implemented")
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
