package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.GameLine

/**
 * A class representing a line segment that can rotate around an origin.
 *
 * @param origin The origin point of the rotating line.
 * @param radius The distance from the origin to the line's endpoint.
 * @param speed The angular speed of rotation in degrees per second.
 * @param degreesOnReset The initial rotation angle in degrees (default is 0) every time the line is reset. The
 * line is reset when the [reset] method is called and also when the line is first created.
 */
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

    /**
     * Gets the current position of the endpoint of the rotating line.
     *
     * @return The current position of the endpoint.
     */
    override fun getMotionValue() = getEndPoint()

    /**
     * Updates the rotation of the line based on the specified angular speed.
     *
     * @param delta The time elapsed since the last update.
     */
    override fun update(delta: Float) {
        degrees += speed * delta
        line.rotation = degrees
    }

    /** Resets the rotation of the line to its initial angle value [degreesOnReset]. */
    override fun reset() {
        degrees = degreesOnReset
    }

    /**
     * Sets the origin and endpoint of the rotating line.
     *
     * @param origin The new origin point.
     * @param endPoint The new endpoint point.
     */
    fun set(origin: Vector2, endPoint: Vector2) {
        setOrigin(origin)
        setStartPoint(origin)
        setEndPoint(endPoint)
    }

    /**
     * Gets the origin point of the rotating line.
     *
     * @return The origin point.
     */
    fun getOrigin() = Vector2(line.originX, line.originY)

    /**
     * Sets the origin point of the rotating line.
     *
     * @param origin The new origin point.
     */
    fun setOrigin(origin: Vector2) = setOrigin(origin.x, origin.y)

    /**
     * Sets the origin point of the rotating line.
     *
     * @param x The x-coordinate of the new origin point.
     * @param y The y-coordinate of the new origin point.
     */
    fun setOrigin(x: Float, y: Float) {
        line.setOrigin(x, y)
    }

    /**
     * Gets the start point of the rotating line.
     *
     * @return The start point of the line.
     */
    fun getStartPoint(): Vector2 {
        val (firstWorldPoint, _) = line.getWorldPoints()
        return firstWorldPoint
    }

    /**
     * Sets the start point of the rotating line.
     *
     * @param startPoint The new start point.
     */
    fun setStartPoint(startPoint: Vector2) = setStartPoint(startPoint.x, startPoint.y)

    /**
     * Sets the start point of the rotating line.
     *
     * @param x The x-coordinate of the new start point.
     * @param y The y-coordinate of the new start point.
     */
    fun setStartPoint(x: Float, y: Float) {
        line.setFirstLocalPoint(x, y)
    }

    /**
     * Gets the endpoint of the rotating line.
     *
     * @return The endpoint of the line.
     */
    fun getEndPoint(): Vector2 {
        val (_, secondWorldPoint) = line.getWorldPoints()
        return secondWorldPoint
    }

    /**
     * Sets the endpoint of the rotating line.
     *
     * @param endPoint The new endpoint point.
     */
    fun setEndPoint(endPoint: Vector2) = setEndPoint(endPoint.x, endPoint.y)

    /**
     * Sets the endpoint of the rotating line.
     *
     * @param x The x-coordinate of the new endpoint point.
     * @param y The y-coordinate of the new endpoint point.
     */
    fun setEndPoint(x: Float, y: Float) {
        line.setSecondLocalPoint(x, y)
    }

    /**
     * Gets a position along the line based on a scalar value.
     *
     * @param scalar The scalar value indicating the position along the line (0.0 to 1.0).
     * @return The position along the line.
     */
    fun getScaledPosition(scalar: Float): Vector2 {
        val endPoint = getEndPoint()
        val x = line.originX + (endPoint.x - line.originX) * scalar
        val y = line.originY + (endPoint.y - line.originY) * scalar
        return Vector2(x, y)
    }

    /**
     * Translates the origin point of the rotating line by the specified amounts.
     *
     * @param x The amount to translate along the x-axis.
     * @param y The amount to translate along the y-axis.
     */
    fun translate(x: Float, y: Float) {
        line.originX += x
        line.originY += y
    }
}
