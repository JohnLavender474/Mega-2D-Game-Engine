package com.engine.entities.contracts

import com.engine.drawables.shapes.DrawableShapesComponent
import com.engine.drawables.shapes.IDrawableShape
import com.engine.entities.IGameEntity

interface IDrawableShapesEntity : IGameEntity {

    fun getDrawableShapesComponent() = getComponent(DrawableShapesComponent::class)!!

    fun addProdShapeSupplier(supplier: () -> IDrawableShape?) {
        getDrawableShapesComponent().prodShapeSuppliers.add(supplier)
    }

    fun addDebugShapeSupplier(supplier: () -> IDrawableShape?) {
        getDrawableShapesComponent().debugShapeSuppliers.add(supplier)
    }

    fun setDebugShapes(debug: Boolean) {
        getDrawableShapesComponent().debug = debug
    }

}