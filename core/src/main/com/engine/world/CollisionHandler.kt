package com.engine.world

interface CollisionHandler {
  fun handleCollision(body1: Body, body2: Body, delta: Float)
}
