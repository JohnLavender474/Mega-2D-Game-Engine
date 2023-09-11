package com.engine.drawables.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * A shape that can be drawn. This interface is used to draw shapes using the [ShapeRenderer].
 *
 * @see ShapeRenderer
 */
fun interface DrawableShape {

  /**
   * Draws this shape.
   *
   * @param renderer the shape renderer to use
   */
  fun draw(renderer: ShapeRenderer)
}
