package com.engine.world

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle

/**
 * A collision handler that handles collisions between bodies. This collision handler is used by
 * default in the [WorldSystem]. This collision handler is stateless. This collision handler handles
 * collisions by moving the dynamic body out of the static body and applying the friction value of
 * [PhysicsData.frictionToApply] from [Body.physics] to [PhysicsData.frictionOnSelf] of the dynamic
 * body's [Body.physics].
 *
 * @see WorldSystem
 * @see Body
 * @see ICollisionHandler
 */
object StandardCollisionHandler : ICollisionHandler {

  /**
   * Handles a collision between two bodies. This collision handler handles collisions by moving the
   * dynamic body out of the static body and applying the friction value of
   * [PhysicsData.frictionToApply] from [Body.physics] to [PhysicsData.frictionOnSelf] of the
   * dynamic body's [Body.physics].
   *
   * @param body1 The first body in the collision.
   * @param body2 The second body in the collision.
   * @see ICollisionHandler.handleCollision
   */
  override fun handleCollision(body1: Body, body2: Body): Boolean {
    if (!body1.physics.collisionOn || !body2.physics.collisionOn) return false

    val dynamicBody: Body
    val staticBody: Body

    if (body1.isBodyType(BodyType.DYNAMIC) && body2.isBodyType(BodyType.STATIC)) {
      dynamicBody = body1
      staticBody = body2
    } else if (body2.isBodyType(BodyType.DYNAMIC) && body1.isBodyType(BodyType.STATIC)) {
      dynamicBody = body2
      staticBody = body1
    } else return false

    val overlap = Rectangle()
    Intersector.intersectRectangles(dynamicBody, staticBody, overlap)

    if (overlap.width == 0f && overlap.height == 0f) return false

    if (overlap.width > overlap.height) {
      dynamicBody.physics.frictionOnSelf.x += staticBody.physics.frictionToApply.x

      if (dynamicBody.y > staticBody.y) dynamicBody.y += overlap.height
      else dynamicBody.y -= overlap.height
    } else {
      dynamicBody.physics.frictionOnSelf.y += staticBody.physics.frictionToApply.y

      if (dynamicBody.x > staticBody.x) dynamicBody.x += overlap.width
      else dynamicBody.x -= overlap.width
    }

    return true
  }
}
