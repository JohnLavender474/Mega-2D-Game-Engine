package com.engine.entities.contracts

import com.engine.animations.AnimationsComponent
import com.engine.entities.IGameEntity

/** An entity that can be animated. */
interface IAnimatedEntity : IGameEntity {

  /**
   * Returns the [AnimationsComponent] of this [IAnimatedEntity].
   *
   * @return the [AnimationsComponent] of this [IAnimatedEntity]
   */
  fun getAnimationsComponent() = getComponent(AnimationsComponent::class)!!

  /**
   * Returns the [AnimationsComponent.animators] of this [IAnimatedEntity].
   *
   * @return the [AnimationsComponent.animators] of this [IAnimatedEntity]
   */
  fun getAnimatorsArray() = getAnimationsComponent().animators
}
