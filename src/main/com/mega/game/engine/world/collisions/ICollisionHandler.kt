package com.mega.game.engine.world.collisions

import com.mega.game.engine.world.WorldSystem
import com.mega.game.engine.world.body.Body

/**
 * A handler for collisions. Used to handle collisions between bodies in the [WorldSystem].
 * Implementations of this interface should be registered with the [WorldSystem] to be used.
 * Implementations of this interface should be stateless.
 *
 * @see Body
 */
interface ICollisionHandler {

    /**
     * Handles a collision between two bodies. Implementations of this method should be stateless.
     * Returns whether the collision was handled.
     *
     * @param body1 The first fixtureBody in the collision.
     * @param body2 The second fixtureBody in the collision.
     * @return Whether the collision was handled.
     */
    fun handleCollision(body1: Body, body2: Body): Boolean
}
