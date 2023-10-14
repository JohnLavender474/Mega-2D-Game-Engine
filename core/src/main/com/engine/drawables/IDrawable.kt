package com.engine.drawables

import com.engine.common.interfaces.IPropertizable
import com.engine.common.interfaces.Updatable

/**
 * An interface for objects that can be drawn. This interface is used to draw objects generically.
 *
 * @param T the type of the drawer
 */
interface IDrawable<T> : Updatable, IPropertizable {

  /**
   * Draws this object using the provided drawer.
   *
   * @param drawer the drawer to use
   */
  fun draw(drawer: T)

  /**
   * Optional method to update this drawable object.
   *
   * @param delta The time in seconds since the last update
   */
  override fun update(delta: Float) {}
}
