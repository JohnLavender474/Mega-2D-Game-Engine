package com.engine.motion

import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

class Pendulum(
    private val length: Float,
    private val gravity: Float,
    private val anchor: Vector2,
    private val targetFPS: Float,
    private val scalar: Float = 1f
) : Updatable, Resettable {

  private var angleVel = 0f
  private var angleAccel = 0f
  private var accumulator = 0f
  private var angle = PI / 2f

  private val end: Vector2 = Vector2()

  override fun update(delta: Float) {
    accumulator += delta
    while (accumulator >= targetFPS) {
      accumulator -= targetFPS
      angleAccel = (gravity / length * sin(angle))
      angleVel += angleAccel * targetFPS * scalar
      angle += angleVel * targetFPS * scalar
    }
    end.set(getPointFromAnchor(length))
  }

  override fun reset() {
    angleVel = 0f
    angleAccel = 0f
    accumulator = 0f
    angle = PI / 2f
    end.setZero()
  }

  fun getPointFromAnchor(dist: Float): Vector2 {
    val point = Vector2()
    point.x = (anchor.x + sin(angle) * dist)
    point.y = (anchor.y + cos(angle) * dist)
    return point
  }
}
