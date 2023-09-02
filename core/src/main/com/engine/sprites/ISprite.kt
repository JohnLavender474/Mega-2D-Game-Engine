package com.engine.sprites

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.engine.common.interfaces.Positional

/**
 * An interface for objects that can be drawn. This interface extends the [Positional] interface and
 * the [Comparable] interface. The priority variable is used to determine the order in which sprites
 * are drawn. Sprites with a higher priority are drawn on top of sprites with a lower priority. The
 * hidden variable is used to determine whether a sprite should be drawn.
 *
 * @see Positional
 * @see Comparable
 */
interface ISprite : Positional, Comparable<ISprite> {

  var priority: Int

  /**
   * Returns whether this sprite is in the specified camera.
   *
   * @param camera the camera to check
   * @return whether this sprite is in the specified camera
   */
  fun isInCamera(camera: Camera): Boolean

  /**
   * Draws this sprite.
   *
   * @param batch the sprite batch to use
   */
  fun draw(batch: Batch)
}
