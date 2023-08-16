package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.extensions.PolylineExtensions.getAsLines
import com.engine.common.shapes.extensions.RectangleExtensions.getAsLines
import com.engine.common.shapes.extensions.RectangleExtensions.getBottomCenterPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getBottomLeftPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getBottomRightPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getCenterLeftPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getCenterPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getCenterRightPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getTopCenterPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getTopLeftPoint
import com.engine.common.shapes.extensions.RectangleExtensions.getTopRightPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setBottomCenterToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setBottomLeftToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setBottomRightToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setCenterLeftToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setCenterRightToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setCenterToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setTopCenterToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setTopLeftToPoint
import com.engine.common.shapes.extensions.RectangleExtensions.setTopRightToPoint
import com.engine.common.shapes.utils.IntersectorUtils

class KRectangle : Rectangle, KShape2D {

    constructor() : super()

    constructor(x: Float, y: Float, width: Float, height: Float) : super(x, y, width, height)

    constructor(rect: Rectangle?) : super(rect)

    override fun setMaxX(maxX: Float) {
        x = maxX - width
    }

    override fun setMaxY(maxY: Float) {
        y = maxY - height
    }

    override fun getMaxX() = x + width

    override fun getMaxY() = y + height

    override fun setCenterX(centerX: Float) {
        setCenter(centerX, getCenter().y)
    }

    override fun setCenterY(centerY: Float) {
        setCenter(getCenter().x, centerY)
    }

    override fun getCenter(): Vector2 = getCenter(Vector2())

    override fun translate(translateX: Float, translateY: Float) {
        x += translateX
        y += translateY
    }

    override fun overlaps(other: KShape2D) =
            when (other) {
                is KRectangle -> Intersector.overlaps(other, this)
                is KCircle -> Intersector.overlaps(other, this)
                is KLine -> {
                    getAsLines().any { IntersectorUtils.intersectLines(it, other, null) }
                }
                is KPolyline -> {
                    getAsLines().any { rLine ->
                        other.getAsLines().any { pLine ->
                            IntersectorUtils.intersectLines(rLine, pLine, null)
                        }
                    }
                }
                else -> false
            }

    override fun getBoundingRectangle() = Rectangle(this)

    override fun positionOnPoint(point: Vector2, position: Position) {
        when (position) {
            Position.TOP_LEFT -> setTopLeftToPoint(point)
            Position.TOP_CENTER -> setTopCenterToPoint(point)
            Position.TOP_RIGHT -> setTopRightToPoint(point)
            Position.CENTER_LEFT -> setCenterLeftToPoint(point)
            Position.CENTER -> setCenterToPoint(point)
            Position.CENTER_RIGHT -> setCenterRightToPoint(point)
            Position.BOTTOM_LEFT -> setBottomLeftToPoint(point)
            Position.BOTTOM_CENTER -> setBottomCenterToPoint(point)
            Position.BOTTOM_RIGHT -> setBottomRightToPoint(point)
        }
    }

    override fun getPositionPoint(position: Position) =
            when (position) {
                Position.TOP_LEFT -> getTopLeftPoint()
                Position.TOP_CENTER -> getTopCenterPoint()
                Position.TOP_RIGHT -> getTopRightPoint()
                Position.CENTER_LEFT -> getCenterLeftPoint()
                Position.CENTER -> getCenterPoint()
                Position.CENTER_RIGHT -> getCenterRightPoint()
                Position.BOTTOM_LEFT -> getBottomLeftPoint()
                Position.BOTTOM_CENTER -> getBottomCenterPoint()
                Position.BOTTOM_RIGHT -> getBottomRightPoint()
            }


}
