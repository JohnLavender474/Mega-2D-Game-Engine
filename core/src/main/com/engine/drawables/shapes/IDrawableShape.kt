package com.engine.drawables.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.engine.drawables.IDrawable

/**
 * A shape that can be drawn. This interface is used to draw shapes using the [ShapeRenderer].
 *
 * @see ShapeRenderer
 */
interface IDrawableShape : IDrawable<ShapeRenderer> {
  var color: Color
}
