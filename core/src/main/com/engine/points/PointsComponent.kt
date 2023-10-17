package com.engine.points

import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * The pointss component. Contains all the pointsMap for an entity.
 *
 * @param pointsMap The pointsMap.
 */
class PointsComponent(
    override val entity: IGameEntity,
    val pointsMap: ObjectMap<String, PointsHandle>
) : IGameComponent {

  /**
   * The pointsMap component. Contains all the pointsMap for an entity.
   *
   * @param _points The pointsMap.
   */
  constructor(
      entity: IGameEntity,
      vararg _points: Pair<String, PointsHandle>
  ) : this(entity, _points.asIterable())

  /**
   * The pointsMap component. Contains all the pointsMap for an entity.
   *
   * @param _points The pointsMap.
   */
  constructor(
      entity: IGameEntity,
      _points: Iterable<Pair<String, PointsHandle>>
  ) : this(
      entity,
      ObjectMap<String, PointsHandle>().apply { _points.forEach { put(it.first, it.second) } })

  override fun reset() = pointsMap.values().forEach { it.reset() }

  /**
   * Gets the points mapped to the given name.
   *
   * @param name The name of the points.
   * @return The points.
   */
  fun getPoints(name: String) = pointsMap[name].points

  /**
   * Gets the [PointsHandle] mapped to the given name.
   *
   * @param name The name of the pointsHandle.
   * @return The pointsHandle.
   */
  fun getPointsHandle(name: String): PointsHandle = pointsMap[name]

  /**
   * Gets the listener [(Points) -> Unit] mapped to the given name.
   *
   * @param name The name of the [PointsHandle] containing the listener.
   * @return The listener
   */
  fun getPointsListener(name: String) = pointsMap[name].listener

  /**
   * Puts the [PointsHandle] into the mpa.
   *
   * @param name The name of the [PointsHandle].
   * @param pointsHandle The [PointsHandle]
   * @return The previous [PointsHandle] mapped to the given name.
   */
  fun putPointsHandle(name: String, pointsHandle: PointsHandle): PointsHandle? =
      pointsMap.put(name, pointsHandle)

  /**
   * Removes the [PointsHandle] mapped to the given name.
   *
   * @param name The name of the [PointsHandle] to remove.
   * @return The removed [PointsHandle].
   */
  fun removePointsHandle(name: String): PointsHandle? = pointsMap.remove(name)
}
