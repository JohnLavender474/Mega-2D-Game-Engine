package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.ICopyable
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
 * @property continueBeyondTarget whether the arc should continue beyond the target position
 */
class ArcMotion(
    startPosition: Vector2,
    targetPosition: Vector2,
    var speed: Float,
    var arcFactor: Float,
    var continueBeyondTarget: Boolean = false
) : IMotion, ICopyable<ArcMotion> {

    companion object {

        /**
         * Computes a bezier point between the start and target positions using the given arc factor.
         *
         * @param t the time value
         * @param arcFactor the arc factor
         * @param startPosition the start position
         * @param targetPosition the target position
         */
        fun computeBezierPoint(t: Float, arcFactor: Float, startPosition: Vector2, targetPosition: Vector2): Vector2 {
            val controlPoint = Vector2(
                ((startPosition.x + targetPosition.x) / 2f) + (arcFactor * (targetPosition.y - startPosition.y)),
                ((startPosition.y + targetPosition.y) / 2f) + (arcFactor * (startPosition.x - targetPosition.x))
            )

            val x =
                ((1 - t).pow(2) * startPosition.x) + (2 * (1 - t) * t * controlPoint.x) + (t.pow(2) * targetPosition.x)
            val y =
                ((1 - t).pow(2) * startPosition.y) + (2 * (1 - t) * t * controlPoint.y) + (t.pow(2) * targetPosition.y)

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

    /**
     * The distance covered by the arc. This value is updated by the [update] method and is used to determine when the
     * arc has reached the target position. This value is equal to the sum of deltas multipled by speed. Private setter,
     * should only be modified internally by the class. Public access is read-only.
     */
    var distanceCovered = 0f
        private set

    /**
     * Convenience property that returns the total distance between the start and target positions. This value is
     * calculated by calling [Vector2.dst] on the start and target positions. This value is read-only.
     */
    val totalDistance: Float
        get() = startPosition.dst(targetPosition)

    private var currentPosition = startPosition.cpy()

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
        distanceCovered += speed * delta

        if (!continueBeyondTarget && distanceCovered >= totalDistance) {
            currentPosition = targetPosition.cpy()
            return
        }

        val t = distanceCovered / totalDistance
        currentPosition = computeBezierPoint(t, arcFactor, startPosition, targetPosition)
    }

    /**
     * Computes the position of the arc at the given time value (distance covered divided by total distance). This does
     * not update the current position of the arc and therefore has no effect on the return value of [getMotionValue].
     * To update the current position of the arc, use the [update] method instead.
     *
     * @param t the time value (distance covered divided by total distance)
     * @return the position of the arc at the given time value
     */
    fun compute(t: Float): Vector2 {
        return computeBezierPoint(t, arcFactor, startPosition, targetPosition)
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
        distanceCovered = 0f
    }

    /**
     * Returns a copy of this [ArcMotion]. The copy will have copies of the [startPosition], [targetPosition], [speed],
     * [arcFactor], and [continueBeyondTarget] properties.
     *
     * @return a copy of this [ArcMotion]
     */
    override fun copy() = ArcMotion(startPosition.cpy(), targetPosition.cpy(), speed, arcFactor, continueBeyondTarget)
}