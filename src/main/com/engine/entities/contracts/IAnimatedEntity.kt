package com.engine.entities.contracts

import com.badlogic.gdx.utils.Array
import com.engine.animations.AnimationsComponent
import com.engine.animations.IAnimator
import com.engine.drawables.sprites.GameSprite
import com.engine.entities.IGameEntity

/** An entity that can be animated. */
interface IAnimatedEntity : IGameEntity {

    /**
     * Returns the array of animators of this [IAnimatedEntity].
     *
     * @return the array of animators of this [IAnimatedEntity]
     */
    val animators: Array<Pair<() -> GameSprite, IAnimator>>
        get() = getAnimationsComponent().animators

    /**
     * Returns the [AnimationsComponent] of this [IAnimatedEntity].
     *
     * @return the [AnimationsComponent] of this [IAnimatedEntity]
     */
    fun getAnimationsComponent() = getComponent(AnimationsComponent::class)!!
}
