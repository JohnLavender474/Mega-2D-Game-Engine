package com.mega.game.engine.world.collisions

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.world.body.Body
import com.mega.game.engine.world.body.BodyType


object StandardCollisionHandler : ICollisionHandler {

    private val gameRectOut = GameRectangle()
    private val rectOut1 = Rectangle()
    private val rectOut2 = Rectangle()
    private val overlap = Rectangle()
    
    override fun handleCollision(body1: Body, body2: Body): Boolean {
        if (!body1.physics.collisionOn || !body2.physics.collisionOn) return false

        val dynamicBody: Body
        val staticBody: Body

        if (body1.type == BodyType.DYNAMIC && body2.type == BodyType.STATIC) {
            dynamicBody = body1
            staticBody = body2
        } else if (body2.type == BodyType.DYNAMIC && body1.type == BodyType.STATIC) {
            dynamicBody = body2
            staticBody = body1
        } else return false

        val bounds1 = dynamicBody.getBounds(gameRectOut).get(rectOut1)
        val bounds2 = dynamicBody.getBounds(gameRectOut).get(rectOut2)
        if (Intersector.intersectRectangles(bounds1, bounds2, overlap)) {
            if (overlap.width > overlap.height) {
                if (dynamicBody.physics.receiveFrictionX)
                    dynamicBody.physics.frictionOnSelf.x += staticBody.physics.frictionToApply.x

                if (dynamicBody.y > staticBody.y) dynamicBody.y += overlap.height
                else dynamicBody.y -= overlap.height
            } else {
                if (dynamicBody.physics.receiveFrictionY)
                    dynamicBody.physics.frictionOnSelf.y += staticBody.physics.frictionToApply.y

                if (dynamicBody.x > staticBody.x) dynamicBody.x += overlap.width
                else dynamicBody.x -= overlap.width
            }

            return true
        }

        return false
    }
}
