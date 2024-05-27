package com.engine.animations

import com.badlogic.gdx.utils.Array
import com.engine.common.GameLogger
import com.engine.components.IGameComponent
import com.engine.drawables.sprites.GameSprite
import com.engine.entities.IGameEntity
import com.engine.entities.contracts.ISpritesEntity
import java.util.function.Supplier

/**
 * A component that can be used to animate a sprite. The component is created with a map of
 * animations that are used to animate the sprite.
 *
 * @param animators the animators that are used to animate the respective sprites
 */
class AnimationsComponent(
    override val entity: IGameEntity,
    val animators: Array<Pair<() -> GameSprite, IAnimator>> = Array()
) : IGameComponent {

    companion object {
        const val TAG = "AnimationsComponent"
    }

    /**
     * Convenience constructor if only one animator. Creates an animations component with the
     * specified sprite supplier and animator.
     *
     * @param spriteSupplier the sprite supplier that is used to supply the sprite to animate
     * @param animator the animator that is used to animate the sprite
     * @see AnimationsComponent
     */
    constructor(
        entity: IGameEntity,
        spriteSupplier: () -> GameSprite,
        animator: IAnimator
    ) : this(
        entity, Array<Pair<() -> GameSprite, IAnimator>>().apply { add(Pair(spriteSupplier, animator)) })

    /**
     * Convenience constructor if using Java [Supplier] to supply the sprite.
     *
     * @param entity the entity that contains the sprite to animate
     * @param spriteSupplier the sprite supplier that is used to supply the sprite to animate
     * @param animator the animator that is used to animate the sprite
     */
    constructor(
        entity: IGameEntity,
        spriteSupplier: Supplier<GameSprite>,
        animator: IAnimator
    ) : this(entity, { spriteSupplier.get() }, animator)

    /**
     * Convenience constructor if the entity is a [ISpritesEntity] where only the first sprite needs to
     * be animated. The first sprite is [ISpritesEntity.firstSprite] which CANNOT BE NULL. This sprite
     * is animated using the specified animator.
     *
     * @param entity the entity that contains the sprite to animate
     * @param animator the animator that is used to animate the sprite
     */
    constructor(
        entity: ISpritesEntity,
        animator: IAnimator
    ) : this(entity, { entity.firstSprite!! }, animator)

    override fun reset() {
        GameLogger.debug(TAG, "reset(): Resetting animations component for entity [$entity]")
        animators.forEach { it.second.reset() }
    }
}
