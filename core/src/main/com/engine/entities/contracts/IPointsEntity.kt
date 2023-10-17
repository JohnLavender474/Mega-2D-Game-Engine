package com.engine.entities.contracts

import com.engine.entities.IGameEntity
import com.engine.points.PointsComponent
import com.engine.points.PointsHandle

/** An entity containing points. */
interface IPointsEntity : IGameEntity {

  /**
   * Returns the [PointsComponent] of this [IGameEntity].
   *
   * @return The [PointsComponent].
   */
  fun getPointsComponent() = getComponent(PointsComponent::class)!!

  /**
   * Gets the points mapped to the given name.
   *
   * @param name The name of the points.
   * @return The points.
   */
  fun getPoints(name: String) = getPointsComponent().getPoints(name)

  /**
   * Gets the [PointsHandle] mapped to the given name.
   *
   * @param name The name of the pointsHandle.
   * @return The pointsHandle.
   */
  fun getPointsHandle(name: String) = getPointsComponent().getPointsHandle(name)

  /**
   * Gets the listener [(Points) -> Unit] mapped to the given name.
   *
   * @param name The name of the [PointsHandle] containing the listener.
   * @return The listener
   */
  fun getPointsListener(name: String) = getPointsComponent().getPointsListener(name)

  /**
   * Resets all the points.
   *
   * @see PointsComponent.reset
   */
  fun resetPoints() = getPointsComponent().reset()

  /**
   * Resets the points mapped to the given name.
   *
   * @param name The name of the points.
   * @see PointsComponent.reset
   */
  fun putPointsHandle(name: String, pointsHandle: PointsHandle) =
      getPointsComponent().putPointsHandle(name, pointsHandle)

  /**
   * Removes the points mapped to the given name.
   *
   * @param name The name of the points.
   * @return The removed points.
   */
  fun removePointsHandle(name: String) = getPointsComponent().removePointsHandle(name)
}
