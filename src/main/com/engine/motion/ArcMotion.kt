package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Updatable
import kotlin.math.pow

/**
 * An [ArcMotion] is an [Updatable] that moves in an arc pattern. The arc is created by computing a bezier point
 * between the start and target positions.
 *
 * @param startPosition the start position of the arc
 * @param targetPosition the target position of the arc
 * @property speed the speed of the arc
 * @property arcFactor the arc factor of the arc
 */
class ArcMotion(
    startPosition: Vector2,
    targetPosition: Vector2,
    var speed: Float,
    var arcFactor: Float
) : IMotion {

    companion object {

        /**
         * Computes a bezier point between the start and target positions using the given arc factor.
         *
         * @param t the time value (should be between 0 and 1)
         * @param arcFactor the arc factor
         * @param startPosition the start position
         * @param targetPosition the target position
         */
        fun computeBezierPoint(t: Float, arcFactor: Float, startPosition: Vector2, targetPosition: Vector2): Vector2 {
            val controlPoint = Vector2(
                ((startPosition.x + targetPosition.x) / 2f) + (arcFactor * (targetPosition.y - startPosition.y)),
                ((startPosition.y + targetPosition.y) / 2f) + (arcFactor * (startPosition.x - targetPosition.x))
            )

            val x = ((1 - t).pow(2) * startPosition.x) + (2 * (1 - t) * t * controlPoint.x) + (t.pow(2) * targetPosition.x)
            val y = ((1 - t).pow(2) * startPosition.y) + (2 * (1 - t) * t * controlPoint.y) + (t.pow(2) * targetPosition.y)

            return Vector2(x, y)
        }
    }


    /**
     * The start position of the arc. When assigning a value to this variable, a copy of the value is stored via
     * [Vector2.cpy] rather than a reference to the original. Also, the [reset] method is called.
     */
    var startPosition = startPosition.cpy()
        set(value) {
            field = value.cpy()
            reset()
        }

    /**
     * The target position of the arc. When assigning a value to this variable, a copy of the value is stored via
     * [Vector2.cpy] rather than a reference to the original. Also, the [reset] method is called.
     */
    var targetPosition = targetPosition.cpy()
        set(value) {
            field = value.cpy()
            reset()
        }

    private var currentPosition = startPosition.cpy()
    private var distanceCovered = 0f

    /**
     * Updates the current position of the arc. The current position is updated by moving the current position towards
     * the target position by the speed of the arc. The distance moved is determined by the speed of the arc and the
     * delta time. If the total distance covered between [update] calls is greater than or equal to the distance
     * between the start and target positions, then the current position is set to the target position (to prevent
     * moving beyond the target).
     *
     * @param delta the time in seconds since the last update
     */
    override fun update(delta: Float) {
        val totalDistance = startPosition.dst(targetPosition)

        distanceCovered += speed * delta

        if (distanceCovered >= totalDistance) {
            currentPosition = targetPosition.cpy()
            return
        }

        val t = (distanceCovered / totalDistance).coerceIn(0f, 1f)
        currentPosition = computeBezierPoint(t, arcFactor, startPosition, targetPosition)
    }

    /**
     * Returns a copy of the current position of the arc.
     *
     * @return a copy of the current position of the arc
     */
    override fun getMotionValue(): Vector2? = currentPosition.cpy()

    /**
     * Resets the arc motion by resetting the current position to the start position and resetting the
     * ratio of distance traveled to zero.
     */
    override fun reset() {
        currentPosition = startPosition.cpy()
    }
}