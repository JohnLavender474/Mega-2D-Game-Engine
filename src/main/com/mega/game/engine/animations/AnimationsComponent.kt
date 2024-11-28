package com.mega.game.engine.animations

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.components.IGameComponent
import com.mega.game.engine.drawables.sprites.GameSprite
import com.mega.game.engine.entities.contracts.ISpritesEntity


class AnimationsComponent(
    val animators: Array<GamePair<() -> GameSprite, IAnimator>> = Array()
) : IGameComponent {

    companion object {
        const val TAG = "AnimationsComponent"
    }

    
    constructor(
        spriteSupplier: () -> GameSprite, animator: IAnimator
    ) : this(Array<GamePair<() -> GameSprite, IAnimator>>().apply { add(GamePair(spriteSupplier, animator)) })


    
    constructor(entity: ISpritesEntity, animator: IAnimator) : this({ entity.firstSprite!! }, animator)

    override fun reset() = animators.forEach { it.second.reset() }
}
