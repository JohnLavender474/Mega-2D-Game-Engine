package com.engine.entities.contracts

import com.engine.drawables.shapes.DrawableShapesComponent
import com.engine.drawables.shapes.IDrawableShape
import com.engine.entities.IGameEntity

/**
 * An [IDrawableShapesEntity] is an [IGameEntity] that contains a [DrawableShapesComponent] for drawing shapes.
 */
interface IDrawableShapesEntity : IGameEntity {

    /**
     * Returns the [DrawableShapesComponent] of this [IDrawableShapesEntity].
     *
     * @return the [DrawableShapesComponent] of this [IDrawableShapesEntity]
     */
    fun getDrawableShapesComponent() = getComponent(DrawableShapesComponent::class)!!

    /**
     * Adds a [IDrawableShape] supplier to the [DrawableShapesComponent] of this [IDrawableShapesEntity].
     *
     * @param supplier the [IDrawableShape] supplier to add
     */
    fun addProdShapeSupplier(supplier: () -> IDrawableShape?) {
        getDrawableShapesComponent().prodShapeSuppliers.add(supplier)
    }

    /**
     * Clears the [IDrawableShape] suppliers of the [DrawableShapesComponent] of this [IDrawableShapesEntity].
     *
     * @see DrawableShapesComponent.prodShapeSuppliers
     */
    fun clearProdShapeSuppliers() {
        getDrawableShapesComponent().prodShapeSuppliers.clear()
    }

    /**
     * Adds a debug [IDrawableShape] supplier to the [DrawableShapesComponent] of this [IDrawableShapesEntity].
     *
     * @param supplier the debug [IDrawableShape] supplier to add
     */
    fun addDebugShapeSupplier(supplier: () -> IDrawableShape?) {
        getDrawableShapesComponent().debugShapeSuppliers.add(supplier)
    }

    /**
     * Clears the debug [IDrawableShape] suppliers of the [DrawableShapesComponent] of this [IDrawableShapesEntity].
     *
     * @see DrawableShapesComponent.debugShapeSuppliers
     */
    fun clearDebugShapeSuppliers() {
        getDrawableShapesComponent().debugShapeSuppliers.clear()
    }

    /**
     * Sets if the [DrawableShapesComponent] of this [IDrawableShapesEntity] should draw debug shapes.
     *
     * @param debug if the [DrawableShapesComponent] of this [IDrawableShapesEntity] should draw debug shapes
     */
    fun setDebugShapes(debug: Boolean) {
        getDrawableShapesComponent().debug = debug
    }

    /**
     * Returns if the [DrawableShapesComponent] of this [IDrawableShapesEntity] should draw debug shapes.
     *
     * @return if the [DrawableShapesComponent] of this [IDrawableShapesEntity] should draw debug shapes
     */
    fun isDebugShapes() = getDrawableShapesComponent().debug

}