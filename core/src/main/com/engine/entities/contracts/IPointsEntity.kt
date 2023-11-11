package com.engine.entities.contracts

import com.engine.entities.IGameEntity
import com.engine.points.Points
import com.engine.points.PointsComponent

/** An entity containing points. */
interface IPointsEntity : IGameEntity {

  /**
   * Returns the [PointsComponent] of this [IGameEntity].
   *
   * @return The [PointsComponent].
   */
  fun getPointsComponent() = getComponent(PointsComponent::class)!!

  /**
   * Gets the points mapped to the given key.
   *
   * @param key The key of the points.
   * @return The points.
   */
  fun getPoints(key: Any) = getPointsComponent().getPoints(key)

  /**
   * Puts the points into the map.
   *
   * @param key The key of the points.
   * @param points The points.
   * @return The previous points mapped to the given key if any
   */
  fun putPoints(key: Any, points: Points) = getPointsComponent().putPoints(key, points)

  /**
   * Puts the points into the map.
   *
   * @param key The key of the points.
   * @param min The minimum value.
   * @param max The maximum value.
   * @param current The current value.
   * @return The previous points mapped to the given key if any
   */
  fun putPoints(key: Any, min: Int, max: Int, current: Int) =
      getPointsComponent().putPoints(key, Points(min, max, current))

  /**
   * Puts the points into the map. The min value will be zero. The current and max values will be
   * equals to [value].
   *
   * @param key The key of the points.
   * @param value The value.
   * @return The previous points mapped to the given key if any
   */
  fun putPoints(key: Any, value: Int) = getPointsComponent().putPoints(key, Points(0, value, value))

  /**
   * Removes the points mapped to the given key.
   *
   * @param key The key of the points.
   * @return The removed points.
   */
  fun removePoints(key: Any) = getPointsComponent().removePoints(key)
}
