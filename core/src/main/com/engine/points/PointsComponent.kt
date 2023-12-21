package com.engine.points

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * The points component. Contains all the points for an entity.
 *
 * @param pointsMap The pointsMap.
 */
class PointsComponent(
    override val entity: IGameEntity,
    val pointsMap: ObjectMap<Any, Points> = ObjectMap(),
    val pointsListeners: ObjectMap<Any, (Points) -> Unit> = ObjectMap()
) : IGameComponent {

  /**
   * The points component. Contains all the points for an entity.
   *
   * @param _points The pointsMap.
   */
  constructor(
      entity: IGameEntity,
      vararg _points: Pair<Any, Points>
  ) : this(entity, _points.asIterable())

  /**
   * The points component. Contains all the points for an entity.
   *
   * @param _points The points iterable.
   */
  constructor(
      entity: IGameEntity,
      _points: Iterable<Pair<Any, Points>>
  ) : this(entity, ObjectMap<Any, Points>().apply { _points.forEach { put(it.first, it.second) } })

  /**
   * Gets the [Points] mapped to the given key.
   *
   * @param key The key of the [Points].
   * @return The points.
   */
  fun getPoints(key: Any): Points = pointsMap[key]

  /**
   * Puts the [Points] into the mpa.
   *
   * @param key The key of the [Points].
   * @param Points The [Points]
   * @return The previous [Points] mapped to the given key.
   */
  fun putPoints(key: Any, Points: Points): Points? = pointsMap.put(key, Points)

  /**
   * Puts the [Points] into the map.
   *
   * @param key The key of the [Points].
   * @param min The minimum value.
   * @param max The maximum value.
   * @param current The current value.
   * @return The previous [Points] mapped to the given key if any
   */
  fun putPoints(key: Any, min: Int, max: Int, current: Int): Points? =
      putPoints(key, Points(min, max, current))

  /**
   * Puts the [Points] into the map. The min value will be zero. The current and max values will be
   * equal to [value].
   *
   * @param key The key of the [Points].
   * @param value The value.
   * @return The previous [Points] mapped to the given key if any
   */
  fun putPoints(key: Any, value: Int): Points? = putPoints(key, Points(0, value, value))

  /**
   * Removes the [Points] mapped to the given key.
   *
   * @param key The key of the [Points] to remove.
   * @return The removed [Points].
   */
  fun removePoints(key: Any): Points? = pointsMap.remove(key)

  /**
   * Adds the given listener to the [Points] mapped to the given key.
   *
   * @param key The key of the [Points].
   * @param listener the listener.
   * @return The previous listener mapped to the given key if any.
   */
  fun putListener(key: Any, listener: (Points) -> Unit) = pointsListeners.put(key, listener)

  /**
   * Removes the listener mapped to the given key.
   *
   * @param key The key of the listener to remove.
   * @return The removed listener.
   */
  fun removeListener(key: Any) = pointsListeners.remove(key)
}
