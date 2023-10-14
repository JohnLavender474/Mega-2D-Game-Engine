package com.engine.drawables.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.engine.common.objects.Properties

/**
 * A handle for a [IDrawableShape]. This handle is used to sort shapes by priority.
 *
 * @param shape the shape
 */
data class DrawableShapeHandle(
    private val shape: IDrawableShape,
    val shapeType: ShapeType,
    override val properties: Properties = Properties()
) : IDrawableShape {

  override fun draw(drawer: ShapeRenderer) = shape.draw(drawer)
}
