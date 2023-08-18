package com.engine.world

import com.badlogic.gdx.math.*
import com.engine.common.interfaces.Resettable
import com.engine.common.shapes.extensions.RectangleExtensions.getCenterPoint

class Body : Resettable {

    val bounds = Rectangle()
    val physicsData = PhysicsData()
    val fixtures: ArrayList<Fixture> = ArrayList()
    val userData: HashMap<String, Any?> = HashMap()

    fun isCenterRightOf(center: Vector2) = bounds.getCenterPoint().x > center.x

    fun isCenterLeftOf(center: Vector2) = bounds.getCenterPoint().x < center.x

    fun isCenterAbove(center: Vector2) = bounds.getCenterPoint().y > center.y

    fun isCenterBelow(center: Vector2) = bounds.getCenterPoint().y < center.y

    fun intersects(body: Body, overlap: Rectangle? = null) = intersects(body.bounds, overlap)

    fun intersects(r: Rectangle, overlap: Rectangle? = null) =
            Intersector.intersectRectangles(bounds, r, overlap)

    fun translate(x: Float, y: Float) {
        bounds.x += x
        bounds.y += y
    }

    override fun reset() {
        physicsData.reset()
        fixtures.forEach { f ->
            val p = bounds.getCenterPoint().add(f.offsetFromBodyCenter)
            f.shape.let {
                when (it) {
                    is Rectangle -> {
                        it.setCenter(p)
                    }
                    is Circle -> {
                        it.setPosition(p)
                    }
                    is Polyline -> {
                        it.setOrigin(p.x, p.y)
                    }
                }
            }
        }
    }
}