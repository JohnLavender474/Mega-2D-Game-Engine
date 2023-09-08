package com.engine.graph

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.MathUtils.floor
import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Resettable
import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.GameShape2D
import com.engine.common.shapes.GameShape2DSupplier

/** The minimum and maximum x and y coordinates of an object. */
data class MinsAndMaxes(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)

/**
 * Gets the minimum and maximum x and y coordinates of the given object.
 *
 * @param obj the object to get the minimum and maximum x and y coordinates of
 * @return the minimum and maximum x and y coordinates of the given object
 */
fun GraphMap.convertToMinsAndMaxes(obj: GameShape2D): MinsAndMaxes {
  val minX = floor(obj.getX() / ppm) - 1
  val minY = floor(obj.getY() / ppm) - 1
  val maxX = ceil(obj.getMaxX() / ppm) + 1
  val maxY = ceil(obj.getMaxY() / ppm) + 1

  return MinsAndMaxes(minX, minY, maxX, maxY)
}

/**
 * Converts the given x and y coordinates to graph coordinates.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the graph coordinates
 */
fun GraphMap.convertToGraphCoordinate(x: Float, y: Float) =
    IntPair((x / ppm).toInt(), (y / ppm).toInt())

/** @see [convertToGraphCoordinate] */
fun GraphMap.convertToGraphCoordinate(v: Vector2) = convertToGraphCoordinate(v.x, v.y)

/** A graph that can be used to store and retrieve objects. */
interface GraphMap : Resettable {

  val width: Int
  val height: Int
  val ppm: Int

  /**
   * Adds the given object to this graph.
   *
   * @param obj the object to add
   * @param shape the shape of the object
   */
  fun add(obj: Any, shape: GameShape2D): Boolean

  /**
   * Adds the given object to this graph.
   *
   * @param obj the object to add
   */
  fun add(obj: GameShape2DSupplier) = add(obj, obj.getGameShape2D())

  /**
   * Adds the given objects to this graph.
   *
   * @param objs the objects to add
   */
  fun addAll(objs: Collection<GameShape2DSupplier>) = objs.forEach { add(it) }

  /** @see [get(Int, Int)] */
  fun get(coordinate: IntPair) = get(coordinate.x, coordinate.y)

  /**
   * Gets the objects at the specified coordinate.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the objects at the specified coordinate
   */
  fun get(x: Int, y: Int): Collection<Any>

  /**
   * Gets the objects in the specified area.
   *
   * @param minX the minimum x coordinate
   * @param minY the minimum y coordinate
   * @param maxX the maximum x coordinate
   * @param maxY the maximum y coordinate
   * @return the objects in the specified area
   */
  fun get(minX: Int, minY: Int, maxX: Int, maxY: Int): Collection<Any>

  /**
   * Gets the objects in the specified area.
   *
   * @param m the minimum and maximum x and y coordinates of the area
   * @return the objects in the specified area
   */
  fun get(m: MinsAndMaxes) = get(m.minX, m.minY, m.maxX, m.maxY)

  /**
   * Gets the objects in the specified area. The area is specified by using the
   * [convertToMinsAndMaxes] method on the supplied [GameRectangle].
   *
   * @param shape the shape to get the area from
   * @return the objects in the specified area
   */
  fun get(shape: GameShape2D) = get(convertToMinsAndMaxes(shape))
}
