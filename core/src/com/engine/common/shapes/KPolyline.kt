package com.engine.common.shapes

import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

class KPolyline: Polyline, KShape2D {

    constructor() : super()

    constructor(vertices: FloatArray?) : super(vertices)

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

    override fun positionOnPoint(point: Vector2, position: Position) {
        TODO("Not yet implemented")
    }

    override fun getPositionPoint(position: Position): Vector2 {
        TODO("Not yet implemented")
    }
}