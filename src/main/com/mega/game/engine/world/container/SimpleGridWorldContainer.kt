package com.mega.game.engine.world.container

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.common.shapes.MinsAndMaxes
import com.mega.game.engine.world.body.Body
import com.mega.game.engine.world.body.IFixture

/**
 * A simple grid-based world container that tracks bodies and fixtures in grid cells. The map is divided into cells,
 * where each cell is of size `width * ppm` and `height * ppm`. Objects (bodies and fixtures) are inserted into the
 * corresponding cell(s) based on their position and bounding shapes.
 *
 * The [bufferOffset] property is used to add fixtures, bodies, or other objects not only to the specified cells but
 * also surrounding cells. For example, if an object that occupies cell [1, 1] is added and the [bufferOffset] value is
 * 1, then the object will be added to the cells with values x += 1 and y += 1, i.e.
 *
 * - [0, 0]
 * - [0, 1]
 * - [0, 2]
 * - [1, 0]
 * - [1, 1]
 * - [1, 2]
 * - [2, 0]
 * - [2, 1]
 * - [2, 2]
 *
 * At the expense of being less optimized or accurate, in some cases it might be preferable to have a buffer area.
 * The default value for [bufferOffset] is 0.
 *
 * The [adjustForExactGridMatch] variable determines whether to adjust coordinates that are exact integer values.
 * This helps avoid issues caused by floating-point precision, where values near exact integers (e.g., 1.0f) might
 * unintentionally cause the block to occupy neighboring grid cells. If set to true, values that are exact integers
 * (like 1.0f or -1.0f) will be adjusted slightly.
 *
 * When [adjustForExactGridMatch] is true:
 * - Positive integer values (e.g., 1.0f) are decreased by [floatRoundingError] to avoid neighboring cell issues.
 * - Negative integer values (e.g., -1.0f) are increased by [floatRoundingError] to adjust appropriately.
 *
 * Be cautious: Due to floating-point precision, values very close to integers (e.g., 1.00000...0001f) might
 * incorrectly be adjusted.
 *
 * @param ppm the pixels per meter of the world representation, used as the width and height of each grid cell.
 * @param bufferOffset the offset that is used as a "buffer area" when adding bodies, fixtures, or other objects
 * @param adjustForExactGridMatch whether to adjust coordinates that are exact integer values
 * @param floatRoundingError the error to use when determining if a floating value is an integer
 */
class SimpleGridWorldContainer(
    var ppm: Int,
    var bufferOffset: Int = 0,
    var adjustForExactGridMatch: Boolean = true,
    var floatRoundingError: Float = MathUtils.FLOAT_ROUNDING_ERROR
) : IWorldContainer {

    private val bodyMap = ObjectMap<IntPair, HashSet<Body>>()
    private val fixtureMap = ObjectMap<IntPair, HashSet<IFixture>>()

    private constructor(
        ppm: Int,
        bufferOffset: Int,
        bodyMap: ObjectMap<IntPair, HashSet<Body>>,
        fixtureMap: ObjectMap<IntPair, HashSet<IFixture>>,
        adjustForExactGridMatch: Boolean,
        floatRoundingError: Float
    ) : this(ppm, bufferOffset, adjustForExactGridMatch, floatRoundingError) {
        this.bodyMap.putAll(bodyMap)
        this.fixtureMap.putAll(fixtureMap)
    }

    private fun adjustCoordinateIfNeeded(value: Float, isMinValue: Boolean) =
        if (adjustForExactGridMatch && MathUtils.isEqual(value % 1f, 0f, floatRoundingError)) {
            if (isMinValue) value + floatRoundingError
            else value - floatRoundingError
        } else value

    private fun getMinsAndMaxes(bounds: GameRectangle): MinsAndMaxes {
        val adjustedMinX = adjustCoordinateIfNeeded(bounds.x, true)
        val adjustedMinY = adjustCoordinateIfNeeded(bounds.y, true)
        val adjustedMaxX = adjustCoordinateIfNeeded(bounds.getMaxX(), false)
        val adjustedMaxY = adjustCoordinateIfNeeded(bounds.getMaxY(), false)

        val minX = MathUtils.floor(adjustedMinX / ppm.toFloat()) - bufferOffset
        val minY = MathUtils.floor(adjustedMinY / ppm.toFloat()) - bufferOffset
        val maxX = MathUtils.floor(adjustedMaxX / ppm.toFloat()) + bufferOffset
        val maxY = MathUtils.floor(adjustedMaxY / ppm.toFloat()) + bufferOffset
        return MinsAndMaxes(minX, minY, maxX, maxY)
    }

    override fun addBody(body: Body): Boolean {
        val bounds = body.getBodyBounds()
        val (minX, minY, maxX, maxY) = getMinsAndMaxes(bounds)
        for (column in minX..maxX) for (row in minY..maxY) {
            val set = bodyMap[column pairTo row] ?: HashSet()
            set.add(body)
            bodyMap.put(column pairTo row, set)
        }
        return true
    }

    override fun addFixture(fixture: IFixture): Boolean {
        val bounds = fixture.getShape().getBoundingRectangle()
        val (minX, minY, maxX, maxY) = getMinsAndMaxes(bounds)
        for (column in minX..maxX) for (row in minY..maxY) {
            val set = fixtureMap[column pairTo row] ?: HashSet()
            set.add(fixture)
            fixtureMap.put(column pairTo row, set)
        }
        return true
    }

    override fun getBodies(x: Int, y: Int): HashSet<Body> {
        val set = bodyMap[x pairTo y]
        return set ?: HashSet()
    }

    override fun getBodies(minX: Int, minY: Int, maxX: Int, maxY: Int): HashSet<Body> {
        val set = HashSet<Body>()
        for (column in minX..maxX) for (row in minY..maxY) bodyMap[column pairTo row]?.let { set.addAll(it) }
        return set
    }

    override fun getFixtures(x: Int, y: Int): HashSet<IFixture> {
        val set = fixtureMap[x pairTo y]
        return set ?: HashSet()
    }

    override fun getFixtures(minX: Int, minY: Int, maxX: Int, maxY: Int): HashSet<IFixture> {
        val set = HashSet<IFixture>()
        for (column in minX..maxX) for (row in minY..maxY) fixtureMap[column pairTo row]?.let { set.addAll(it) }
        return set
    }

    override fun getObjects(x: Int, y: Int): HashSet<Any> {
        val set = HashSet<Any>()
        bodyMap[x pairTo y]?.let { set.addAll(it) }
        fixtureMap[x pairTo y]?.let { set.addAll(it) }
        return set
    }

    override fun getObjects(minX: Int, minY: Int, maxX: Int, maxY: Int): HashSet<Any> {
        val set = HashSet<Any>()
        for (column in minX..maxX) for (row in minY..maxY) {
            bodyMap[column pairTo row]?.let { set.addAll(it) }
            fixtureMap[column pairTo row]?.let { set.addAll(it) }
        }
        return set
    }

    override fun clear() {
        bodyMap.clear()
        fixtureMap.clear()
    }

    override fun copy() =
        SimpleGridWorldContainer(ppm, bufferOffset, bodyMap, fixtureMap, adjustForExactGridMatch, floatRoundingError)

    override fun toString(): String {
        val nonEmptyBodies = bodyMap.filter { it.value.isNotEmpty() }.map { "${it.key}=${it.value.size} bodies" }

        val nonEmptyFixtures = fixtureMap.filter { it.value.isNotEmpty() }.map { "${it.key}=${it.value.size} fixtures" }

        // Construct the final string with filtered entries
        return "SimpleGridWorldContainer[" + "ppm=$ppm, " +
                "bodies={${nonEmptyBodies.joinToString(separator = ", ")}}, " +
                "fixtures={${
                    nonEmptyFixtures.joinToString(
                        separator = ", "
                    )
                }}]"
    }
}
