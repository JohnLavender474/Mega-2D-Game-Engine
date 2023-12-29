package com.engine.drawables.shapes

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to collect shapes to be drawn. The map of shapes is NOT cleared in each
 * update cycle.
 *
 * @param shapesArraySupplier The supplier that supplies the map of shapes
 * @param debug Whether the debug shapes should be drawn when the game is in debug mode
 */
open class DrawableShapeSystem(
    private val shapesArraySupplier: () -> Array<IDrawableShape>,
    var debug: Boolean = false
) : GameSystem(DrawableShapeComponent::class) {

  /**
   * Creates a [DrawableShapeSystem] where the provided [OrderedMap] is used to store the shapes to
   * be drawn.
   *
   * @param shapesArray The [OrderedMap] to store the shapes to be drawn
   * @see [DrawableShapeSystem]
   */
  constructor(shapesArray: Array<IDrawableShape>) : this({ shapesArray })

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    val shapes = shapesArraySupplier()

    entities.forEach {
      val shapeComponent = it.getComponent(DrawableShapeComponent::class)!!

      shapeComponent.prodShapeSuppliers.forEach { shape -> shapes.add(shape()) }

      if (debug && shapeComponent.debug)
          shapeComponent.debugShapeSuppliers.forEach { shape -> shapes.add(shape()) }
    }
  }
}
