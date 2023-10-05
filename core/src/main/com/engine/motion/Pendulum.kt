package com.engine.motion

import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2

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

  fun getPointFromAnchor(distance: Float): Vector2 {
    val point = Vector2()
    point.x = (anchor.x + sin(angle) * distance)
    point.y = (anchor.y + cos(angle) * distance)
    return point
  }

  override fun getMotionValue() = Vector2(endPoint)

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

  override fun reset() {
    angleVel = 0f
    angleAccel = 0f
    accumulator = 0f
    angle = PI / 2f
    endPoint.setZero()
  }
}
