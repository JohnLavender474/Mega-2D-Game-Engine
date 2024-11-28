package com.mega.game.engine.motion

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.shapes.GameLine


class RotatingLine(
    origin: Vector2, radius: Float, var speed: Float, var degreesOnReset: Float = 0f
) : IMotion {

    val line = GameLine()
    var degrees = degreesOnReset

    init {
        val endPoint = origin.cpy().add(radius, 0f)
        set(origin.cpy(), endPoint)
        line.rotation = degrees
    }

    
    override fun getMotionValue() = getEndPoint()

    
    override fun update(delta: Float) {
        degrees += speed * delta
        line.rotation = degrees
    }

    
    override fun reset() {
        degrees = degreesOnReset
    }

    
    fun set(origin: Vector2, endPoint: Vector2) {
        setOrigin(origin)
        setStartPoint(origin)
        setEndPoint(endPoint)
    }

    
    fun getOrigin() = Vector2(line.originX, line.originY)

    
    fun setOrigin(origin: Vector2) = setOrigin(origin.x, origin.y)

    
    fun setOrigin(x: Float, y: Float) {
        line.setOrigin(x, y)
    }

    
    fun getStartPoint(): Vector2 {
        val (firstWorldPoint, _) = line.calculateWorldPoints()
        return firstWorldPoint
    }

    
    fun setStartPoint(startPoint: Vector2) = setStartPoint(startPoint.x, startPoint.y)

    
    fun setStartPoint(x: Float, y: Float) {
        line.setFirstLocalPoint(x, y)
    }

    
    fun getEndPoint(): Vector2 {
        val (_, secondWorldPoint) = line.calculateWorldPoints()
        return secondWorldPoint
    }

    
    fun setEndPoint(endPoint: Vector2) = setEndPoint(endPoint.x, endPoint.y)

    
    fun setEndPoint(x: Float, y: Float) {
        line.setSecondLocalPoint(x, y)
    }

    
    fun getScaledPosition(scalar: Float): Vector2 {
        val endPoint = getEndPoint()
        val x = line.originX + (endPoint.x - line.originX) * scalar
        val y = line.originY + (endPoint.y - line.originY) * scalar
        return Vector2(x, y)
    }

    
    fun translate(x: Float, y: Float) {
        line.originX += x
        line.originY += y
    }
}
