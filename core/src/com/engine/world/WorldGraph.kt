package com.engine.world

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.engine.common.interfaces.Resettable
import com.engine.common.shapes.extensions.CircleExtensions.getBoundingRectangle
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class WorldGraph(val width: Int, val height: Int, val ppm: Int) : Resettable {

    private val bodies: HashMap<Pair<Int, Int>, ArrayList<Body>> = HashMap()
    private val fixtures: HashMap<Pair<Int, Int>, ArrayList<Fixture>> = HashMap()

    fun getFixtures(x: Int, y: Int) = fixtures.getOrDefault(Pair(x, y), ArrayList())

    fun getBodies(x: Int, y: Int) = bodies.getOrDefault(Pair(x, y), ArrayList())

    fun addBody(body: Body) {
        val m = getMinsAndMaxes(body.bounds)
        for (x in m[0] until m[2]) {
            for (y in m[1] until m[3]) {
                bodies.computeIfAbsent(Pair(x, y)) { ArrayList() }.add(body)
            }
        }
    }

    fun addFixture(fixture: Fixture) {
        val b = fixture.shape.let {
            when (it) {
                is Rectangle -> it
                is Circle -> it.getBoundingRectangle()
                is Polyline -> it.boundingRectangle
                else -> throw IllegalStateException("Unsupported Shape2D")
            }
        }
    }

    private fun getMinsAndMaxes(bounds: Rectangle): IntArray {
        val minX = floor(bounds.x / ppm).toInt() - 1
        val minY = floor(bounds.y / ppm).toInt() - 1
        val maxX = ceil((bounds.x + bounds.width) / ppm).toInt() + 1
        val maxY = ceil((bounds.y + bounds.height) / ppm).toInt() + 1
        return intArrayOf(max(0, minX), max(0, minY), min(width, maxX), min(height, maxY))
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}