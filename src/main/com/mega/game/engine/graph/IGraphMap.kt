package com.mega.game.engine.graph

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.MathUtils.floor
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.objects.IntPair
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.common.shapes.IGameShape2D
import com.mega.game.engine.common.shapes.IGameShape2DSupplier

/** The minimum and maximum first and second coordinates of an object. */
data class MinsAndMaxes(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)

/**
 * Gets the minimum and maximum first and second coordinates of the given object using the values of
 * [IGameShape2D.getX], [IGameShape2D.getY], [IGameShape2D.getMaxX], and [IGameShape2D.getMaxY].
 *
 * @param obj the object to get the minimum and maximum first and second coordinates of
 * @return the minimum and maximum first and second coordinates of the given object
 */
fun IGraphMap.convertToMinsAndMaxes(obj: IGameShape2D): MinsAndMaxes {
    val minX = floor(obj.getX() / ppm) - 1
    val minY = floor(obj.getY() / ppm) - 1
    val maxX = ceil(obj.getMaxX() / ppm) + 1
    val maxY = ceil(obj.getMaxY() / ppm) + 1

    return MinsAndMaxes(minX, minY, maxX, maxY)
}

/**
 * Converts the given first and second coordinates to graph coordinates.
 *
 * @param worldX the first coordinate
 * @param worldY the second coordinate
 * @return the graph coordinates
 */
fun IGraphMap.convertToGraphCoordinate(worldX: Float, worldY: Float): IntPair {
    var graphX = (worldX / ppm).toInt()
    var graphY = (worldY / ppm).toInt()

    if (graphX < x) graphX = x else if (graphX >= x + width) graphX = x + width - 1
    if (graphY < y) graphY = y else if (graphY >= y + height) graphY = y + height - 1

    return graphX pairTo graphY
}

/** @see [convertToGraphCoordinate] */
fun IGraphMap.convertToGraphCoordinate(v: Vector2) = convertToGraphCoordinate(v.x, v.y)

/**
 * Converts the given coordinate to its respective world node.
 *
 * @param x the x of the coordinate
 * @param y the y of the coordinate
 * @return the world node
 */
fun IGraphMap.convertToWorldNode(x: Int, y: Int) =
    GameRectangle().setSize(ppm.toFloat()).setPosition(ppm * x.toFloat(), ppm * y.toFloat())

/** @see [convertToWorldNode] */
fun IGraphMap.convertToWorldNode(coordinate: IntPair) =
    convertToWorldNode(coordinate.first, coordinate.second)

/**
 * Checks if the given coordinates are out of bounds.
 *
 * @param _x the first coordinate
 * @param _y the second coordinate
 * @return true if the given coordinates are out of bounds, false otherwise
 */
fun IGraphMap.isOutOfBounds(_x: Int, _y: Int) =
    _x < x || _y < y || _x >= x + width || _y >= y + height

/**
 * Checks if the given coordinate is out of bounds.
 *
 * @param coordinate the coordinate
 * @return true if the given coordinate is out of bounds, false otherwise
 */
fun IGraphMap.isOutOfBounds(coordinate: IntPair) =
    isOutOfBounds(coordinate.first, coordinate.second)

/** A graph that can be used to store and retrieve objects. */
interface IGraphMap : Resettable {

    val x: Int
    val y: Int
    val width: Int
    val height: Int
    val ppm: Int

    /**
     * Adds the given shape to this graph. By default, it calls [add] with the shape used as both the
     * object and the shape.
     *
     * @param shape the shape to add
     * @return true if the shape was added, false otherwise
     */
    fun add(shape: IGameShape2D) = add(shape, shape)

    /**
     * Adds the given object to this graph using the provided shape. The shape is NOT added, only the
     * provided object.
     *
     * @param obj the object to add
     * @param shape the shape of the object
     * @return true if the object was added, false otherwise
     */
    fun add(obj: Any, shape: IGameShape2D): Boolean

    /**
     * Adds the given object to this graph. By default, it calls [add] with the shape of the object.
     *
     * @param obj the object to add
     * @return true if the object was added, false otherwise
     */
    fun add(obj: IGameShape2DSupplier) = add(obj, obj.getGameShape2D())

    /** @see [get(Int, Int)] */
    fun get(coordinate: IntPair) = get(coordinate.first, coordinate.second)

    /**
     * Gets the objects at the specified coordinate.
     *
     * @param x the first coordinate
     * @param y the second coordinate
     * @return the objects at the specified coordinate
     */
    fun get(x: Int, y: Int): Iterable<Any>

    /**
     * Gets the objects in the specified area.
     *
     * @param minX the minimum first coordinate
     * @param minY the minimum second coordinate
     * @param maxX the maximum first coordinate
     * @param maxY the maximum second coordinate
     * @return the objects in the specified area
     */
    fun get(minX: Int, minY: Int, maxX: Int, maxY: Int): Iterable<Any>

    /**
     * Gets the objects in the specified area. By default, calls [get] with the values of
     * [MinsAndMaxes].
     *
     * @param m the minimum and maximum first and second coordinates of the area
     * @return the objects in the specified area
     */
    fun get(m: MinsAndMaxes) = get(m.minX, m.minY, m.maxX, m.maxY)

    /**
     * Gets the objects in the specified area. The area is specified by using the
     * [convertToMinsAndMaxes] method on the supplied [GameRectangle]. By default, converts the given
     * [IGameShape2D] to a [MinsAndMaxes] instance using the [convertToMinsAndMaxes] method.
     *
     * @param shape the shape to get the area from
     * @return the objects in the specified area
     */
    fun get(shape: IGameShape2D) = get(convertToMinsAndMaxes(shape))
}
