package com.mega.game.engine.entities.contracts

import com.mega.game.engine.animations.AnimationsComponent
import com.mega.game.engine.animations.IAnimator
import com.mega.game.engine.drawables.sprites.GameSprite
import com.mega.game.engine.entities.IGameEntity

interface IAnimatedEntity : IGameEntity {

    val animationsComponent: AnimationsComponent
        get() {
            val key = AnimationsComponent::class
            return getComponent(key)!!
        }

    fun putAnimator(sprite: GameSprite, animator: IAnimator) =
        animationsComponent.putAnimator(sprite, animator)

    fun putAnimator(key: Any, sprite: GameSprite, animator: IAnimator) =
        animationsComponent.putAnimator(key, sprite, animator)
}
