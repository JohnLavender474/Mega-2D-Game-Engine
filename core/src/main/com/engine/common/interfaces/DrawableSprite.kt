package com.engine.common.interfaces

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** An interface for objects that can be drawn. */
fun interface DrawableSprite {

  /** Draws this object. */
  fun draw(batch: SpriteBatch)
}
