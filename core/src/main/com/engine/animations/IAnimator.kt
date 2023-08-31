package com.engine.animations

import com.badlogic.gdx.graphics.g2d.Sprite

/** An animator that can be used to animate a sprite. */
fun interface IAnimator {

  /**
   * Animates the specified sprite.
   *
   * @param sprite the sprite to animate
   * @param delta the time in seconds since the last update
   */
  fun animate(sprite: Sprite, delta: Float)
}
