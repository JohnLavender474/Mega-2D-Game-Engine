package com.engine.points

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent

/**
 * The pointss component. Contains all the points for an entity.
 *
 * @param points The points.
 */
class PointsComponent(val points: ObjectMap<String, PointsHandle>) : IGameComponent {

  /**
   * The points component. Contains all the points for an entity.
   *
   * @param _points The points.
   */
  constructor(vararg _points: Pair<String, PointsHandle>) : this(_points.asIterable())

  /**
   * The points component. Contains all the points for an entity.
   *
   * @param _points The points.
   */
  constructor(
      _points: Iterable<Pair<String, PointsHandle>>
  ) : this(ObjectMap<String, PointsHandle>().apply { _points.forEach { put(it.first, it.second) } })
}
