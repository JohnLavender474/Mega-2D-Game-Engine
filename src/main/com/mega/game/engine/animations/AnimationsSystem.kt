package com.mega.game.engine.animations

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem


class AnimationsSystem : GameSystem(AnimationsComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return
        entities.forEach { entity ->
            val animationsComponent = entity.getComponent(AnimationsComponent::class)
            animationsComponent?.animators?.forEach { e ->
                val spriteSupplier = e.first
                val animator = e.second
                animator.animate(spriteSupplier(), delta)
            }
        }
    }
}
