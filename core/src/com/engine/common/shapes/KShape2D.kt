package com.engine.common.shapes

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

interface KShape2D : Shape2D {

    fun getCenter(): Vector2

    fun setCenterX(centerX: Float)

    fun setCenterY(centerY: Float)

    fun getMaxX(): Float

    fun getMaxY(): Float

    fun setMaxX(maxX: Float)

    fun setMaxY(maxY: Float)

    fun overlaps(other: KShape2D): Boolean

    fun getBoundingRectangle(): Rectangle

    fun positionOnPoint(point: Vector2, position: Position)

    fun getPositionPoint(position: Position): Vector2

}