package com.mega.game.engine.world.body

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.common.interfaces.Resettable

/**
 * Physics data for a [Body]. Contains information about the physics of the body.
 *
 * @param gravity The gravity to apply to the body.
 * @param velocity The velocity of the body.
 * @param velocityClamp The maximum velocity of the body.
 * @param frictionToApply The friction to apply to other bodies when colliding with them.
 * @param frictionOnSelf The friction to apply to this body.
 * @param defaultFrictionOnSelf The default friction to apply to this body. Value of
 *   [frictionOnSelf] is reset to this value.
 * @param gravityOn Whether gravity is applied to this body.
 * @param collisionOn Whether collisions are applied to this body.
 * @see Body
 * @see com.mega.game.engine.world.WorldSystem
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
    var applyFrictionX: Boolean = true,
    var applyFrictionY: Boolean = true,
    var receiveFrictionX: Boolean = true,
    var receiveFrictionY: Boolean = true
) : Resettable, ICopyable<PhysicsData> {

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
            applyFrictionX,
            applyFrictionY
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
                "collisionOn=$collisionOn, applyFrictionX=$applyFrictionX, applyFrictionY=$applyFrictionY)"
}
