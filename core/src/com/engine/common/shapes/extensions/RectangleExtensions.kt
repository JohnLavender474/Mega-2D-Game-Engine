package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.*
import com.badlogic.gdx.math.collision.BoundingBox
import com.engine.common.shapes.KLine
import com.engine.common.shapes.extensions.PolylineExtensions.getAsLines

object RectangleExtensions {

    fun Rectangle.overlaps(c: Circle) = Intersector.overlaps(c, this)

    fun Rectangle.overlaps(p: Polyline) = p.getAsLines().any {
        Intersector.intersectSegmentRectangle(it.point1, it.point2, this)
    }

    fun Rectangle.getAsLines() = listOf(
            KLine(getTopLeftPoint(), getTopRightPoint()),
            KLine(getBottomLeftPoint(), getBottomRightPoint()),
            KLine(getBottomLeftPoint(), getTopLeftPoint()),
            KLine(getBottomRightPoint(), getTopRightPoint())
    )

    fun Rectangle.setTopLeftToPoint(topLeftPoint: Vector2): Rectangle =
            setPosition(topLeftPoint.x, topLeftPoint.y - height)

    fun Rectangle.getTopLeftPoint() = Vector2(x, y + height)

    fun Rectangle.setTopCenterToPoint(topCenterPoint: Vector2): Rectangle =
            setPosition(topCenterPoint.x - (width / 2f), topCenterPoint.y - height)

    fun Rectangle.getTopCenterPoint() = Vector2(x + (width / 2f), y + height)

    fun Rectangle.setTopRightToPoint(topRightPoint: Vector2): Rectangle =
            setPosition(topRightPoint.x - width, topRightPoint.y - height)

    fun Rectangle.getTopRightPoint() = Vector2(x + width, y + height)

    fun Rectangle.setCenterLeftToPoint(centerLeftPoint: Vector2): Rectangle =
            this.setPosition(centerLeftPoint.x, centerLeftPoint.y - (this.height / 2f))

    fun Rectangle.getCenterLeftPoint(): Vector2 = Vector2(this.x, this.y + (this.height / 2f))

    fun Rectangle.setCenterToPoint(centerPoint: Vector2): Rectangle = this.setCenter(centerPoint)

    fun Rectangle.getCenterPoint(): Vector2 = this.getCenter(Vector2())

    fun Rectangle.setCenterRightToPoint(centerRightPoint: Vector2): Rectangle =
            this.setPosition(centerRightPoint.x - this.width, centerRightPoint.y - (this.height / 2f))

    fun Rectangle.getCenterRightPoint(): Vector2 =
            Vector2(this.x + this.width, this.y + (this.height / 2f))

    fun Rectangle.setBottomLeftToPoint(bottomLeftPoint: Vector2): Rectangle = this.setPosition(bottomLeftPoint)

    fun Rectangle.getBottomLeftPoint(): Vector2 = Vector2(this.x, this.y)

    fun Rectangle.setBottomCenterToPoint(bottomCenterPoint: Vector2): Rectangle =
            this.setPosition(bottomCenterPoint.x - this.width / 2f, bottomCenterPoint.y)

    fun Rectangle.getBottomCenterPoint(): Vector2 = Vector2(this.x + this.width / 2f, this.y)

    fun Rectangle.setBottomRightToPoint(bottomRightPoint: Vector2): Rectangle =
            this.setPosition(bottomRightPoint.x - this.width, bottomRightPoint.y)

    fun Rectangle.getBottomRightPoint(): Vector2 = Vector2(this.x + this.width, this.y)

    fun Rectangle.toBoundingBox() = BoundingBox(Vector3(x, y, 0f), Vector3(x + width, y + height, 0f))
}