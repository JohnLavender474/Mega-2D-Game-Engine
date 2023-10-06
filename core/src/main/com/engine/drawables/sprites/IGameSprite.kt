package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.interfaces.Positional
import com.engine.common.interfaces.Sizable

/**
 * An interface for objects that can be drawn. This interface extends the [Positional] interface and
 * the [Comparable] interface. The priority variable is used to determine the order in which sprites
 * are drawn. Sprites with a higher priority are drawn on top of sprites with a lower priority. The
 * hidden variable is used to determine whether a sprite should be drawn.
 *
 * @see Positional
 * @see Comparable
 */
interface IGameSprite : IDrawableSprite, Positional, Sizable, Comparable<IGameSprite> {

  var priority: Int

  /**
   * Sets the region of this sprite.
   *
   * @param region the region to set
   * @see TextureRegion
   */
  fun setRegion(region: TextureRegion?)

  /**
   * Sets the texture of this sprite.
   *
   * @param texture the texture to set
   * @see Texture
   */
  fun setTexture(texture: Texture?)
}
