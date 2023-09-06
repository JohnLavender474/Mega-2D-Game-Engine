package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.engine.GameComponent
import com.engine.common.shapes.GameShape2D

/**
 * A component that holds a list of [Motion]s and a list of functions that are called when the
 * [Motion]s are updated. The object to be moved by the motion value should be a [GameShape2D].
 */
class MotionComponent : GameComponent {

  internal val motions = ArrayList<Pair<Motion, (Vector2) -> Unit>>()

  /**
   * Adds a [Motion] to this component. The function is called when the [Motion] is updated and a
   * value has been obtained from [Motion.getMotionValue].
   *
   * @param motion the [Motion] to add
   * @param function the function to call when the [Motion] is updated
   * @return if the [Motion] and function pair was added
   */
  fun add(motion: Motion, function: (Vector2) -> Unit) = motions.add(motion to function)

  /** Resets the motions in this component */
  override fun reset() = motions.map { it.first }.forEach { it.reset() }
}
