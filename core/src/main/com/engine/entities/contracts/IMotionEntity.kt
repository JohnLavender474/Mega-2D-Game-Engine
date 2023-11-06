package com.engine.entities.contracts

import com.engine.entities.IGameEntity
import com.engine.motion.IMotion
import com.engine.motion.MotionComponent

/**
 * An entity that can be moved by a motion value. The motion value is obtained from an [IMotion]
 * that is stored in a [MotionComponent].
 */
interface IMotionEntity : IGameEntity {

  /**
   * Returns the [MotionComponent] of this entity. Throws exception if no [MotionComponent] has been
   * added.
   *
   * @return the [MotionComponent] of this entity
   */
  fun getMotionComponent() = getComponent(MotionComponent::class)

  /**
   * Returns the motions of this entity. Throws exception if no [MotionComponent] has been added.
   *
   * @return the motions of this entity
   */
  fun getMotions() = getMotionComponent()!!.motions

  /**
   * Adds a [IMotion] to this entity.
   *
   * @param key the key to associate with the [IMotion]
   * @param definition the [IMotion] and function pair
   * @return the prior [IMotion] value if any, or null
   */
  fun putMotion(key: Any, definition: MotionComponent.MotionDefinition) =
      getMotionComponent()!!.put(key, definition)
}
