package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.engine.common.shapes.GameShape2D
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A component that holds a list of [IMotion]s and a list of functions that are called when the
 * [IMotion]s are updated. The object to be moved by the motion value should be a [GameShape2D].
 */
class MotionComponent(override val entity: IGameEntity) : IGameComponent {

  internal val motions = Array<Pair<IMotion, (Vector2) -> Unit>>()

  /**
   * Adds a [IMotion] to this component. The function is called when the [IMotion] is updated and a
   * value has been obtained from [IMotion.getMotionValue].
   *
   * @param motion the [IMotion] to add
   * @param function the function to call when the [IMotion] is updated
   * @return if the [IMotion] and function pair was added
   */
  fun add(motion: IMotion, function: (Vector2) -> Unit) = motions.add(motion to function)

  /** Resets the motions in this component */
  override fun reset() = motions.map { it.first }.forEach { it.reset() }
}
