package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch

/** An interface for objects that can be drawn. */
fun interface IDrawableSprite {

  /**
   * Draws this object.
   *
   * @param batch the batch to use
   */
  fun draw(batch: Batch)
}
