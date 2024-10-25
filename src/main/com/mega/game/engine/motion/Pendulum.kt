package com.mega.game.engine.motion

import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.motion.Pendulum.Companion.DEFAULT_DISTURBANCE_THRESHOLD
import com.mega.game.engine.motion.Pendulum.Companion.DEFAULT_RANDOM_FORCE
import kotlin.math.abs

/**
 * A class representing a pendulum motion.
 *
 * @property length The length of the pendulum.
 * @property gravity The acceleration due to gravity.
 * @property anchor The anchor point for the pendulum.
 * @property targetFPS The target frames per second for updates.
 * @property scalar A scaling factor for motion calculations (default is 1.0).
 * @property defaultAngle The default angle to start the pendulum at. Defaults to (PI / 2.0).
 * @property damping The damping factor to simulate energy loss (e.g. air resistance) per second. A higher value means
 * more resistance (default is 0.0).
 * @property disturbanceThreshold Threshold to detect near-vertical positioning, in which case a random force needs to
 * be applied. Default is [DEFAULT_DISTURBANCE_THRESHOLD].
 * @property randomForce Supplier for the random force to apply when the pendulum is nearly vertical. Default lambda
 * returns between -[DEFAULT_RANDOM_FORCE] and [DEFAULT_RANDOM_FORCE].
 */
class Pendulum(
    var length: Float,
    var gravity: Float,
    var anchor: Vector2,
    var targetFPS: Float,
    var scalar: Float = 1f,
    var defaultAngle: Float = PI / 2f,
    var damping: Float = 0f,
    var disturbanceThreshold: Float = DEFAULT_DISTURBANCE_THRESHOLD,
    var randomForce: () -> Float = { random(-DEFAULT_RANDOM_FORCE, DEFAULT_RANDOM_FORCE) }
) : IMotion {

    companion object {
        const val DEFAULT_DISTURBANCE_THRESHOLD = 0.001f
        const val DEFAULT_RANDOM_FORCE = 0.001f
    }

    /**
     * The current angle of the pendulum.
     */
    var angle = defaultAngle

    private val endPoint = Vector2()
    private var angleVel = 0f
    private var angleAccel = 0f
    private var accumulator = 0f

    /**
     * Computes a point at a specified distance from the anchor point on the pendulum.
     *
     * @param distance The distance from the anchor point.
     * @return The point at the specified distance from the anchor point.
     */
    fun getPointFromAnchor(distance: Float): Vector2 {
        val point = Vector2()
        point.x = anchor.x + sin(angle) * distance
        point.y = anchor.y + cos(angle) * distance
        return point
    }

    /**
     * Applies additional force to increase swing speed.
     *
     * @param force The amount of force to apply, increasing angular velocity.
     */
    fun applyForce(force: Float) {
        angleVel += force
    }

    /**
     * Determines if the pendulum is swinging clockwise or counterclockwise.
     *
     * @return 1 if the pendulum is swinging clockwise, -1 if swinging counterclockwise, 0 if it's not moving.
     */
    fun getSwingDirection() = when {
        angleVel < 0 -> 1
        angleVel > 0 -> -1
        else -> 0
    }

    /**
     * Gets the current motion value represented as a 2D vector.
     *
     * @return The current position of the pendulum's endpoint.
     */
    override fun getMotionValue() = Vector2(endPoint)

    /**
     * Updates the pendulum's motion.
     *
     * @param delta The time elapsed since the last update.
     */
    override fun update(delta: Float) {
        accumulator += delta
        while (accumulator >= targetFPS) {
            accumulator -= targetFPS
            if (abs(defaultAngle % PI) < disturbanceThreshold) angleVel += randomForce()
            angleAccel = (gravity / length * sin(angle))
            angleVel += angleAccel * targetFPS * scalar
            angleVel *= (1 - (damping * delta))
            angle += angleVel * targetFPS * scalar
        }
        endPoint.set(getPointFromAnchor(length))
    }

    /** Resets the pendulum to its initial state. */
    override fun reset() {
        angleVel = 0f
        angleAccel = 0f
        accumulator = 0f
        angle = defaultAngle
        endPoint.setZero()
    }
}
