package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.drawables.shapes.DrawableShapesComponent;
import com.mega.game.engine.drawables.shapes.IDrawableShape;
import kotlin.jvm.functions.Function0;
import kotlin.reflect.KClass;


public interface IDrawableShapesEntity extends IGameEntity {

    
    default DrawableShapesComponent getDrawableShapesComponent() {
        KClass<DrawableShapesComponent> key = ClassInstanceUtils.convertToKClass(DrawableShapesComponent.class);
        return getComponent(key);
    }

    
    default void addProdShapeSupplier(Function0<IDrawableShape> supplier) {
        getDrawableShapesComponent().getProdShapeSuppliers().add(supplier);
    }

    
    default void clearProdShapeSuppliers() {
        getDrawableShapesComponent().getProdShapeSuppliers().clear();
    }

    
    default void addDebugShapeSupplier(Function0<IDrawableShape> supplier) {
        getDrawableShapesComponent().getDebugShapeSuppliers().add(supplier);
    }

    
    default void clearDebugShapeSuppliers() {
        getDrawableShapesComponent().getDebugShapeSuppliers().clear();
    }

    
    default boolean isDebugShapes() {
        return getDrawableShapesComponent().getDebug();
    }

    
    default void setDebugShapes(boolean debug) {
        getDrawableShapesComponent().setDebug(debug);
    }
}
