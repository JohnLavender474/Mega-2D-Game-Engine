package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Resettable

data class PhysicsData(
    var gravity: Vector2 = Vector2(),
    var velocity: Vector2 = Vector2(),
    var velocityClamp: Vector2 = Vector2(),
    var frictionToApply: Vector2 = Vector2(),
    var frictionOnSelf: Vector2 = Vector2(),
    var defaultFrictionOnSelf: Vector2 = Vector2(),
    var gravityOn: Boolean = true,
    var collisionOn: Boolean = true,
    var takeFrictionFromOthers: Boolean = true
) : Resettable {

  override fun reset() {
    velocity.setZero()
    frictionOnSelf.set(defaultFrictionOnSelf)
  }
}
