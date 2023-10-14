package com.engine.animations

import com.badlogic.gdx.utils.ObjectMap
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
    val animations: ObjectMap<ISprite, IAnimator> = ObjectMap(),
) : IGameComponent {

  /** Animates the specified sprite. */
  constructor(
      entity: IGameEntity,
      sprite: ISprite,
      animator: IAnimator
  ) : this(entity, ObjectMap<ISprite, IAnimator>().apply { put(sprite, animator) })

  override fun reset() {
    animations.values().forEach { it.reset() }
  }
}
