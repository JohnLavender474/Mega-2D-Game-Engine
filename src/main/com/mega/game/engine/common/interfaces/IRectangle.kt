package com.mega.game.engine.common.interfaces

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.enums.Position
import com.mega.game.engine.common.shapes.GamePolygon

interface IRectangle : ISizable {

    fun getPositionPoint(position: Position, out: Vector2) = when (position) {
        Position.TOP_LEFT -> getTopLeftPoint(out)
        Position.TOP_CENTER -> getTopCenterPoint(out)
        Position.TOP_RIGHT -> getTopRightPoint(out)
        Position.CENTER_LEFT -> getCenterLeftPoint(out)
        Position.CENTER -> getCenterPoint(out)
        Position.CENTER_RIGHT -> getCenterRightPoint(out)
        Position.BOTTOM_LEFT -> getBottomLeftPoint(out)
        Position.BOTTOM_CENTER -> getBottomCenterPoint(out)
        Position.BOTTOM_RIGHT -> getBottomRightPoint(out)
    }

    fun getPosition(out: Vector2): Vector2 = out.set(getX(), getY())

    fun setCenter(x: Float, y: Float): IRectangle {
        setX(x - getWidth() / 2f)
        setY(y - getHeight() / 2f)
        return this
    }

    fun setCenter(center: Vector2) = setCenter(center.x, center.y)

    fun getCenter(out: Vector2): Vector2

    fun set(x: Float, y: Float, width: Float, height: Float): IRectangle {
        setPosition(x, y)
        setSize(width, height)
        return this
    }

    fun setTopLeftToPoint(topLeftPoint: Vector2): IRectangle {
        setPosition(topLeftPoint.x, topLeftPoint.y - getHeight())
        return this
    }

    fun getTopLeftPoint(out: Vector2): Vector2 = out.set(getX(), getY() + getHeight())

    fun setTopCenterToPoint(topCenterPoint: Vector2): IRectangle {
        setPosition(topCenterPoint.x - (getWidth() / 2f), topCenterPoint.y - getHeight())
        return this
    }

    fun getTopCenterPoint(out: Vector2): Vector2 = out.set(getX() + (getWidth() / 2f), getY() + getHeight())

    fun setTopRightToPoint(topRightPoint: Vector2): IRectangle {
        setPosition(topRightPoint.x - getWidth(), topRightPoint.y - getHeight())
        return this
    }

    fun getTopRightPoint(out: Vector2): Vector2 = out.set(getX() + getWidth(), getY() + getHeight())

    fun setCenterLeftToPoint(centerLeftPoint: Vector2): IRectangle {
        this.setPosition(centerLeftPoint.x, centerLeftPoint.y - (getHeight() / 2f))
        return this
    }

    fun getCenterLeftPoint(out: Vector2): Vector2 = out.set(getX(), getY() + (getHeight() / 2f))

    fun setCenterToPoint(centerPoint: Vector2): IRectangle {
        this.setCenter(centerPoint)
        return this
    }

    fun getCenterPoint(out: Vector2): Vector2 = this.getCenter(out)

    fun setCenterRightToPoint(centerRightPoint: Vector2): IRectangle {
        this.setPosition(centerRightPoint.x - this.getWidth(), centerRightPoint.y - (this.getHeight() / 2f))
        return this
    }

    fun getCenterRightPoint(out: Vector2): Vector2 =
        out.set(this.getX() + this.getWidth(), this.getY() + (this.getHeight() / 2f))

    fun setBottomLeftToPoint(bottomLeftPoint: Vector2): IRectangle {
        this.setPosition(bottomLeftPoint)
        return this
    }

    fun getBottomLeftPoint(out: Vector2): Vector2 = out.set(this.getX(), this.getY())

    fun setBottomCenterToPoint(bottomCenterPoint: Vector2): IRectangle {
        this.setPosition(bottomCenterPoint.x - this.getWidth() / 2f, bottomCenterPoint.y)
        return this
    }

    fun getBottomCenterPoint(out: Vector2): Vector2 = out.set(this.getX() + this.getWidth() / 2f, this.getY())

    fun setBottomRightToPoint(bottomRightPoint: Vector2): IRectangle {
        this.setPosition(bottomRightPoint.x - this.getWidth(), bottomRightPoint.y)
        return this
    }

    fun getBottomRightPoint(out: Vector2): Vector2 = out.set(this.getX() + this.getWidth(), this.getY())

    fun toPolygon(out: GamePolygon): GamePolygon {
        out.setLocalVertices(floatArrayOf(0f, 0f, getWidth(), 0f, getWidth(), getHeight(), 0f, getHeight()))
        out.setPosition(getX(), getY())
        return out
    }

    fun getX(): Float

    fun getY(): Float

    fun getMaxX() = getX() + getWidth()

    fun getMaxY() = getY() + getHeight()

    fun setX(x: Float): IRectangle

    fun setY(y: Float): IRectangle

    fun setMaxX(x: Float) = setX(getX() - getWidth())

    fun setMaxY(y: Float) = setY(getY() - getHeight())

    fun setPosition(x: Float, y: Float): IRectangle {
        setX(x)
        setY(y)
        return this
    }

    fun setPosition(position: Vector2) = setPosition(position.x, position.y)

    fun translate(x: Float, y: Float) = setPosition(getX() + x, getY() + y)

    fun translate(delta: Vector2) = translate(delta.x, delta.y)
}