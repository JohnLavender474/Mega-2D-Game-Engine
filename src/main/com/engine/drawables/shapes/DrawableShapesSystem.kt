package com.engine.drawables.shapes

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to collect shapes to be drawn. The map of shapes is NOT cleared in each
 * update cycle.
 *
 * @param shapesCollector The function that should collect the shapes to be drawn
 * @param debug Whether the debug shapes should be drawn when the game is in debug mode
 */
open class DrawableShapesSystem(
    private val shapesCollector: (IDrawableShape) -> Unit,
    var debug: Boolean = false
) : GameSystem(DrawableShapesComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        entities.forEach { e ->
            val shapeComponent = e.getComponent(DrawableShapesComponent::class)!!

            shapeComponent.prodShapeSuppliers.forEach { shapeSupplier ->
                shapeSupplier()?.let { shapesCollector.invoke(it) }
            }

            if (debug && shapeComponent.debug)
                shapeComponent.debugShapeSuppliers.forEach { shapeSupplier ->
                    shapeSupplier()?.let { shapesCollector.invoke(it) }
                }
        }
    }
}
