package com.engine.drawables.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to collect shapes to be drawn. The map of shapes is NOT cleared in each
 * update cycle.
 *
 * @param shapes the map to hold the shapes to be rendered
 */
class DrawableShapeSystem(
    private val shapes: OrderedMap<ShapeRenderer.ShapeType, Array<IDrawableShape>>
) : GameSystem(DrawableShapeComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    // collect the shapes
    entities.forEach {
      it.getComponent(DrawableShapeComponent::class)?.shapes?.forEach { shape ->
        if (!shapes.containsKey(shape.shapeType)) {
          shapes.put(shape.shapeType, Array())
        }
        val list = shapes[shape.shapeType]
        list.add(shape)
      }
    }
  }
}
