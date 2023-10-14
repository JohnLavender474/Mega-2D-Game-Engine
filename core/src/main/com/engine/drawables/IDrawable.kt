package com.engine.drawables

/**
 * An interface for objects that can be drawn. This interface is used to draw objects generically.
 *
 * @param T the type of the drawer
 */
interface IDrawable<T> {

  /**
   * Draws this object using the provided drawer.
   *
   * @param drawer the drawer to use
   */
  fun draw(drawer: T)
}
