package com.engine.animations

import com.badlogic.gdx.utils.Array
import com.engine.common.GameLogger
import com.engine.components.IGameComponent
import com.engine.drawables.sprites.ISprite
import com.engine.entities.IGameEntity

/**
 * A component that can be used to animate a sprite. The component is created with a map of
 * animations that are used to animate the sprite.
 *
 * @param animations the animations that are used to animate the sprite
 */
class AnimationsComponent(
    override val entity: IGameEntity,
    val animations: Array<Pair<() -> ISprite, IAnimator>> = Array()
) : IGameComponent {

  companion object {
    const val TAG = "AnimationsComponent"
  }

  /** Animates the specified sprite. */
  constructor(
      entity: IGameEntity,
      spriteSupplier: () -> ISprite,
      animator: IAnimator
  ) : this(
      entity, Array<Pair<() -> ISprite, IAnimator>>().apply { add(Pair(spriteSupplier, animator)) })

  override fun reset() {
    GameLogger.debug(TAG, "reset(): Resetting animations component for entity [$entity]")
    animations.forEach { it.second.reset() }
  }
}
