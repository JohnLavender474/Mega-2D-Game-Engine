package com.engine.world

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle

object StandardCollisionHandler : CollisionHandler {

  override fun handleCollision(body1: Body, body2: Body, delta: Float) {
    val dynamicBody: Body
    val staticBody: Body
    if (body1.isBodyType(BodyType.DYNAMIC) && body2.isBodyType(BodyType.STATIC)) {
      dynamicBody = body1
      staticBody = body2
    } else if (body2.isBodyType(BodyType.DYNAMIC) && body1.isBodyType(BodyType.STATIC)) {
      dynamicBody = body2
      staticBody = body1
    } else {
      return
    }
    val overlap = Rectangle()
    Intersector.intersectRectangles(dynamicBody, staticBody, overlap)
    if (overlap.width > overlap.height) {
      dynamicBody.physicsData.frictionOnSelf.x += staticBody.physicsData.frictionToApply.x
      if (dynamicBody.y > staticBody.y) {
        dynamicBody.y += overlap.height
      } else {
        dynamicBody.y -= overlap.height
      }
    } else {
      dynamicBody.physicsData.frictionOnSelf.y += staticBody.physicsData.frictionToApply.y
      if (dynamicBody.x > staticBody.x) {
        dynamicBody.x += overlap.width
      } else {
        dynamicBody.x -= overlap.width
      }
    }
  }
}
