package com.engine.drawables.shapes

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A [DrawableShapeComponent] is a [IGameComponent] that contains a list of [IDrawableShape]s that
 * can be drawn.
 *
 * @param entity the [IGameEntity] that this [DrawableShapeComponent] belongs to
 * @param shapes the [IDrawableShape]s that can be drawn
 */
class DrawableShapeComponent(override val entity: IGameEntity, val shapes: Array<IDrawableShape>) :
    IGameComponent {

  /**
   * Creates a [DrawableShapeComponent] with the given [IDrawableShape]s.
   *
   * @param entity the [IGameEntity] that this [DrawableShapeComponent] belongs to
   * @param shapes the [IDrawableShape]s that can be drawn
   */
  constructor(entity: IGameEntity, vararg shapes: IDrawableShape) : this(entity, Array(shapes))
}
