package com.engine.drawables.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to draw shapes. This system requires a [ShapeRenderer].
 *
 * @param shapeRenderer the shape renderer to use
 * @see ShapeRenderer
 */
class DrawableShapeSystem(private val shapeRenderer: ShapeRenderer) :
    GameSystem(DrawableShapeComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    val map = ObjectMap<ShapeRenderer.ShapeType, Array<DrawableShapeHandle>>()
    entities.forEach {
      it.getComponent(DrawableShapeComponent::class)?.shapes?.forEach { shape ->
        if (!map.containsKey(shape.shapeType)) {
          map.put(shape.shapeType, Array())
        }
        val list = map[shape.shapeType]
        list.add(shape)
      }
    }

    map.forEach {
      shapeRenderer.begin(it.key)
      it.value.forEach { shape -> shape.draw(shapeRenderer) }
      shapeRenderer.end()
    }
  }
}
