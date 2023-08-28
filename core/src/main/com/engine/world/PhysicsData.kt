package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Resettable

/**
 * Physics data for a [Body]. Contains information about the physics of the body.
 *
 * @param gravity The gravity to apply to the body.
 * @param velocity The velocity of the body.
 * @param velocityClamp The maximum velocity of the body.
 * @param frictionToApply The friction to apply to other bodies when colliding with them.
 * @param frictionOnSelf The friction to apply to this body in the next [WorldSystem] update.
 * @param defaultFrictionOnSelf The default friction to apply to this body. Value of
 *   [frictionOnSelf] is reset to this value at the end of each [WorldSystem] update.
 * @param gravityOn Whether gravity is applied to this body.
 * @param collisionOn Whether collisions are applied to this body.
 * @param takeFrictionFromOthers Whether this body takes friction from other bodies.
 * @see Body
 */
open class PhysicsData(
    var gravity: Vector2 = Vector2(),
    var velocity: Vector2 = Vector2(),
    var velocityClamp: Vector2 = Vector2(),
    var frictionToApply: Vector2 = Vector2(),
    var frictionOnSelf: Vector2 = Vector2(1f, 1f),
    var defaultFrictionOnSelf: Vector2 = Vector2(1f, 1f),
    var gravityOn: Boolean = true,
    var collisionOn: Boolean = true,
    var takeFrictionFromOthers: Boolean = true
) : Resettable {

  fun resetToDefault() {
    gravity.setZero()
    velocity.setZero()
    velocityClamp.setZero()
    frictionToApply.setZero()
    defaultFrictionOnSelf.set(1f, 1f)
    frictionOnSelf.set(defaultFrictionOnSelf)
    gravityOn = true
    collisionOn = true
    takeFrictionFromOthers = true
  }

  override fun reset() {
    velocity.setZero()
    frictionOnSelf.set(defaultFrictionOnSelf)
  }
}
