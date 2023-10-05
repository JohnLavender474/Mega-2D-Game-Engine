package com.engine.animations

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.ObjectMap

/**
 * An animator that can be used to animate a sprite. The animator is created with a key supplier
 * that is used to determine which animation to play. The key supplier is called every update and
 * the returned key is used to determine which animation to play. The animator is also created with
 * a map of animations that are used to animate the sprite.
 *
 * @param keySupplier the key supplier that is used to determine which animation to play
 * @param animations the animations that are used to animate the sprite
 * @see IAnimator
 */
class Animator(
    val keySupplier: () -> String?,
    val animations: ObjectMap<String, IAnimation>,
) : IAnimator {

  val currentAnimation: IAnimation?
    get() = if (currentKey != null) animations[currentKey] else null

  var currentKey: String? = null
    private set

  override fun animate(sprite: Sprite, delta: Float) {
    val priorKey = currentKey
    val nextKey = keySupplier()
    if (priorKey != nextKey) {
      currentAnimation?.reset()
    }
    currentKey = nextKey
    currentAnimation?.let {
      it.update(delta)
      sprite.setRegion(it.getCurrentRegion())
    }
  }

  override fun reset() {
    currentKey = null
  }
}
