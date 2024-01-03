package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.interfaces.Resettable
import com.engine.common.shapes.IGameShape2D
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A component that holds a list of [IMotion]s and a list of functions that are called when the
 * [IMotion]s are updated. The object to be moved by the motion value should be a [IGameShape2D].
 */
class MotionComponent(override val entity: IGameEntity) : IGameComponent {

  /**
   * A definition of a [IMotion] and function pair. The function is called when the [IMotion] is
   * updated and a value has been obtained from [IMotion.getMotionValue].
   *
   * @param motion the [IMotion]
   * @param function the function
   */
  data class MotionDefinition(val motion: IMotion, val function: (Vector2, Float) -> Unit) :
      Resettable {
    override fun reset() = motion.reset()
  }

  val motions = OrderedMap<Any, MotionDefinition>()

  /**
   * Adds a [IMotion] to this component. The function is called when the [IMotion] is updated and a
   * value has been obtained from [IMotion.getMotionValue].
   *
   * @param key the key to associate with the [IMotion]
   * @param definition the [IMotion] and function pair
   * @return if the [IMotion] and function pair was added
   */
  fun put(key: Any, definition: MotionDefinition): MotionDefinition? = motions.put(key, definition)

  /** Resets the motions in this component */
  override fun reset() = motions.values().forEach { it.reset() }
}
