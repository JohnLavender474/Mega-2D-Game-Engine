package com.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

class KCircle: Circle, KShape2D {

    constructor() : super()

    constructor(x: Float, y: Float, radius: Float) : super(x, y, radius)

    constructor(position: Vector2?, radius: Float) : super(position, radius)

    constructor(circle: Circle?) : super(circle)

    constructor(center: Vector2?, edge: Vector2?) : super(center, edge)

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
}