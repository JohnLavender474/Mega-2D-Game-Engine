package com.engine.motion

import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2

/**
 * A class representing a pendulum motion.
 *
 * @param length The length of the pendulum.
 * @param gravity The acceleration due to gravity.
 * @param anchor The anchor point for the pendulum.
 * @param targetFPS The target frames per second for updates.
 * @param scalar A scaling factor for motion calculations (default is 1.0).
 */
class Pendulum(
    var length: Float,
    var gravity: Float,
    var anchor: Vector2,
    var targetFPS: Float,
    var scalar: Float = 1f
) : IMotion {

  private var angleVel = 0f
  private var angleAccel = 0f
  private var accumulator = 0f
  private var angle = PI / 2f

  private val endPoint = Vector2()

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
      angleAccel = (gravity / length * sin(angle))
      angleVel += angleAccel * targetFPS * scalar
      angle += angleVel * targetFPS * scalar
    }
    endPoint.set(getPointFromAnchor(length))
  }

  /** Resets the pendulum to its initial state. */
  override fun reset() {
    angleVel = 0f
    angleAccel = 0f
    accumulator = 0f
    angle = PI / 2f
    endPoint.setZero()
  }
}
