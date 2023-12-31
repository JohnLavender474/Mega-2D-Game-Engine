package com.engine.animations

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.GameLogger
import com.engine.common.extensions.objectMapOf
import com.engine.drawables.sprites.ISprite

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

  companion object {
    const val TAG = "Animator"
    const val DEFAULT_KEY = "Default"
  }

  /**
   * Convenience constructor if only one animation is needed. Creates an animator with the specified
   * animation. The animator is created with a default key supplier that always returns
   * [DEFAULT_KEY]. Also, the [animations] map is created with the [DEFAULT_KEY] and the specified
   * animation.
   *
   * @param animation the animation to animate the sprite with
   */
  constructor(animation: IAnimation) : this({ DEFAULT_KEY }, objectMapOf(DEFAULT_KEY to animation))

  val currentAnimation: IAnimation?
    get() = if (currentKey != null) animations[currentKey] else null

  var currentKey: String? = null
    private set

  override fun animate(sprite: ISprite, delta: Float) {
    val nextKey = keySupplier()
    if (currentKey != nextKey) {
      GameLogger.debug(TAG, "animate(): Switching animation from [$currentKey] to [$nextKey]")
      currentAnimation?.reset()
    }
    currentKey = nextKey

    currentAnimation?.let {
      it.update(delta)
      it.getCurrentRegion()?.let { region -> sprite.setRegion(region) }
    }
  }

  override fun reset() {
    currentKey = null
    animations.values().forEach { it.reset() }
  }
}
