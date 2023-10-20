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
 * @param shapesMapSupplier The supplier that supplies the map of shapes
 */
open class DrawableShapeSystem(
    private val shapesMapSupplier: () -> OrderedMap<ShapeRenderer.ShapeType, Array<IDrawableShape>>
) : GameSystem(DrawableShapeComponent::class) {

  /**
   * Creates a [DrawableShapeSystem] where the provided [OrderedMap] is used to store the shapes to
   * be drawn.
   *
   * @param shapes The [OrderedMap] to store the shapes to be drawn
   * @see [DrawableShapeSystem]
   */
  constructor(shapes: OrderedMap<ShapeRenderer.ShapeType, Array<IDrawableShape>>) : this({ shapes })

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    // collect the shapes
    val shapes = shapesMapSupplier()
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
