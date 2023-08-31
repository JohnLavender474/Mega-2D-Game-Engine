package com.engine.drawing

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** An interface for objects that can be drawn. */
fun interface Drawable {

  /** Draws this object. */
  fun draw(batch: SpriteBatch)
}
