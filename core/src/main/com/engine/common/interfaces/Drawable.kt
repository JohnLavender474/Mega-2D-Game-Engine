package com.engine.common.interfaces

import com.badlogic.gdx.graphics.g2d.Batch

/** An interface for objects that can be drawn. */
fun interface Drawable {

  /**
   * Draws this object.
   *
   * @param batch the batch to use
   */
  fun draw(batch: Batch)
}
