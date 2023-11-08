package com.engine.drawables.shapes

import com.badlogic.gdx.utils.Array
import com.engine.common.extensions.toGdxArray
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A [DrawableShapeComponent] is a [IGameComponent] that contains an of suppliers for
 * [IDrawableShape]s that can be drawn.
 *
 * @param entity the [IGameEntity] that this [DrawableShapeComponent] belongs to
 * @param shapeSuppliers the [IDrawableShape]s that can be drawn
 */
class DrawableShapeComponent(
    override val entity: IGameEntity,
    val shapeSuppliers: Array<() -> IDrawableShape>
) : IGameComponent {

  /**
   * Creates a [DrawableShapeComponent] with the given [IDrawableShape] suppliers.
   *
   * @param entity the [IGameEntity] that this [DrawableShapeComponent] belongs to
   * @param shapeSuppliers the suppliers for [IDrawableShape]s that can be drawn
   */
  constructor(
      entity: IGameEntity,
      vararg shapeSuppliers: () -> IDrawableShape
  ) : this(entity, Array(shapeSuppliers))

  /**
   * Creates a [DrawableShapeComponent] with the given [IDrawableShape]s.
   *
   * @param entity the [IGameEntity] that this [DrawableShapeComponent] belongs to
   * @param shapes the [IDrawableShape]s that can be drawn
   */
  constructor(
      entity: IGameEntity,
      vararg shapes: IDrawableShape
  ) : this(entity, shapes.map { { it } }.toGdxArray())
}
