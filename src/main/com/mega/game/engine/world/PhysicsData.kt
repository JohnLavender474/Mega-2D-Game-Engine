package com.mega.game.engine.world

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.common.interfaces.Resettable

/**
 * Physics data for a [Body]. Contains information about the physics of the fixtureBody.
 *
 * @param gravity The gravity to apply to the fixtureBody.
 * @param velocity The velocity of the fixtureBody.
 * @param velocityClamp The maximum velocity of the fixtureBody.
 * @param frictionToApply The friction to apply to other bodies when colliding with them.
 * @param frictionOnSelf The friction to apply to this fixtureBody in the next [WorldSystem] update.
 * @param defaultFrictionOnSelf The default friction to apply to this fixtureBody. Value of
 *   [frictionOnSelf] is reset to this value at the end of each [WorldSystem] update.
 * @param gravityOn Whether gravity is applied to this fixtureBody.
 * @param collisionOn Whether collisions are applied to this fixtureBody.
 * @param takeFrictionFromOthers Whether this fixtureBody takes friction from other bodies.
 * @see Body
 */
class PhysicsData(
    var gravity: Vector2 = Vector2(),
    var velocity: Vector2 = Vector2(),
    var velocityClamp: Vector2 = Vector2(Float.MAX_VALUE, Float.MAX_VALUE),
    var frictionToApply: Vector2 = Vector2(),
    var frictionOnSelf: Vector2 = Vector2(1f, 1f),
    var defaultFrictionOnSelf: Vector2 = Vector2(1f, 1f),
    var gravityOn: Boolean = true,
    var collisionOn: Boolean = true,
    var takeFrictionFromOthers: Boolean = true
) : Resettable, ICopyable<PhysicsData> {

    /** Resets the [PhysicsData] to its default values. */
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

    /**
     * Creates a copy of this [PhysicsData].
     *
     * @return A copy of this [PhysicsData].
     */
    override fun copy() =
        PhysicsData(
            Vector2(gravity),
            Vector2(velocity),
            Vector2(velocityClamp),
            Vector2(frictionToApply),
            Vector2(frictionOnSelf),
            Vector2(defaultFrictionOnSelf),
            gravityOn,
            collisionOn,
            takeFrictionFromOthers
        )

    /** Sets the [velocity] to zero and the [frictionOnSelf] to [defaultFrictionOnSelf]. */
    override fun reset() {
        velocity.setZero()
        frictionOnSelf.set(defaultFrictionOnSelf)
    }

    override fun toString() =
        "PhysicsData(gravity=$gravity, velocity=$velocity, velocityClamp=$velocityClamp, " +
                "frictionToApply=$frictionToApply, frictionOnSelf=$frictionOnSelf, " +
                "defaultFrictionOnSelf=$defaultFrictionOnSelf, gravityOn=$gravityOn, " +
                "collisionOn=$collisionOn, takeFrictionFromOthers=$takeFrictionFromOthers)"
}
