package com.mega.game.engine.entities.contracts

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.animations.AnimationsComponent
import com.mega.game.engine.animations.IAnimator
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.drawables.sprites.GameSprite
import com.mega.game.engine.entities.IGameEntity

interface IAnimatedEntity : IGameEntity {

    val animationsComponent: AnimationsComponent
        get() {
            val key = AnimationsComponent::class
            return getComponent(key)!!
        }

    val animators: Array<GamePair<Function0<GameSprite>, IAnimator>>
        get() = this.animationsComponent.animators
}
