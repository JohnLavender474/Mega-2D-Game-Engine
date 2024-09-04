package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.drawables.shapes.DrawableShapesComponent;
import com.mega.game.engine.drawables.shapes.IDrawableShape;
import kotlin.jvm.functions.Function0;
import kotlin.reflect.KClass;

/**
 * An IDrawableShapesEntity is an GameEntity that contains a DrawableShapesComponent for drawing shapes.
 */
public interface IDrawableShapesEntity extends IGameEntity {

    /**
     * Returns the DrawableShapesComponent of this IDrawableShapesEntity.
     *
     * @return the DrawableShapesComponent of this IDrawableShapesEntity
     */
    default DrawableShapesComponent getDrawableShapesComponent() {
        KClass<DrawableShapesComponent> key = ClassInstanceUtils.convertToKClass(DrawableShapesComponent.class);
        return getComponent(key);
    }

    /**
     * Adds an IDrawableShape supplier to the DrawableShapesComponent of this IDrawableShapesEntity.
     *
     * @param supplier the IDrawableShape supplier to add
     */
    default void addProdShapeSupplier(Function0<IDrawableShape> supplier) {
        getDrawableShapesComponent().getProdShapeSuppliers().add(supplier);
    }

    /**
     * Clears the IDrawableShape suppliers of the DrawableShapesComponent of this IDrawableShapesEntity.
     *
     * @see DrawableShapesComponent#getProdShapeSuppliers()
     */
    default void clearProdShapeSuppliers() {
        getDrawableShapesComponent().getProdShapeSuppliers().clear();
    }

    /**
     * Adds a debug IDrawableShape supplier to the DrawableShapesComponent of this IDrawableShapesEntity.
     *
     * @param supplier the debug IDrawableShape supplier to add
     */
    default void addDebugShapeSupplier(Function0<IDrawableShape> supplier) {
        getDrawableShapesComponent().getDebugShapeSuppliers().add(supplier);
    }

    /**
     * Clears the debug IDrawableShape suppliers of the DrawableShapesComponent of this IDrawableShapesEntity.
     *
     * @see DrawableShapesComponent#getDebugShapeSuppliers()
     */
    default void clearDebugShapeSuppliers() {
        getDrawableShapesComponent().getDebugShapeSuppliers().clear();
    }

    /**
     * Returns if the DrawableShapesComponent of this IDrawableShapesEntity should draw debug shapes.
     *
     * @return if the DrawableShapesComponent of this IDrawableShapesEntity should draw debug shapes
     */
    default boolean isDebugShapes() {
        return getDrawableShapesComponent().getDebug();
    }

    /**
     * Sets if the DrawableShapesComponent of this IDrawableShapesEntity should draw debug shapes.
     *
     * @param debug if the DrawableShapesComponent of this IDrawableShapesEntity should draw debug shapes
     */
    default void setDebugShapes(boolean debug) {
        getDrawableShapesComponent().setDebug(debug);
    }
}
