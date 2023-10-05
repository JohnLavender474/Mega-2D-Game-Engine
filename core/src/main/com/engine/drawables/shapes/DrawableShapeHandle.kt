package com.engine.drawables.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

/**
 * A handle for a [IDrawableShape]. This handle is used to sort shapes by priority.
 *
 * @param shape the shape
 */
data class DrawableShapeHandle(
    private val shape: IDrawableShape,
    val shapeType: ShapeType,
) : IDrawableShape {

  override fun draw(renderer: ShapeRenderer) = shape.draw(renderer)
}
