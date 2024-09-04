package com.mega.game.engine.drawables.shapes

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem
import java.util.function.Consumer

/**
 * A system that can be used to collect shapes to be drawn. The map of shapes is NOT cleared in each
 * update cycle.
 *
 * @param shapesCollector The function that should collect the shapes to be drawn
 * @param debug Whether the debug shapes should be drawn when the game is in debug mode
 */
class DrawableShapesSystem(
    private val shapesCollector: (IDrawableShape) -> Unit,
    var debug: Boolean = false
) : GameSystem(DrawableShapesComponent::class) {

    /**
     * Creates a [DrawableShapesSystem] with the given [shapesCollector].
     *
     * @param shapesCollector The function that should collect the shapes to be drawn
     * @param debug Whether the debug shapes should be drawn when the game is in debug mode
     */
    constructor(
        shapesCollector: Consumer<IDrawableShape>,
        debug: Boolean = false
    ) : this(shapesCollector::accept, debug)

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
