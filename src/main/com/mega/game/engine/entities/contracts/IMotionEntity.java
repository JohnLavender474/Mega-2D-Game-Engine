package com.mega.game.engine.entities.contracts;

import com.badlogic.gdx.utils.OrderedMap;
import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.motion.IMotion;
import com.mega.game.engine.motion.MotionComponent;
import kotlin.reflect.KClass;


public interface IMotionEntity extends IGameEntity {


    default MotionComponent getMotionComponent() {
        KClass<MotionComponent> key = ClassInstanceUtils.convertToKClass(MotionComponent.class);
        return getComponent(key);
    }


    default OrderedMap<Object, MotionComponent.MotionDefinition> getMotionDefinitions() {
        return getMotionComponent().getDefinitions();
    }


    default MotionComponent.MotionDefinition getMotionDefinition(Object key) {
        return getMotionDefinitions().get(key);
    }


    default IMotion getMotion(Object key) {
        MotionComponent.MotionDefinition definition = getMotionDefinition(key);
        return definition != null ? definition.getMotion() : null;
    }


    default MotionComponent.MotionDefinition putMotionDefinition(Object key,
                                                                 MotionComponent.MotionDefinition definition) {
        return getMotionComponent().put(key, definition);
    }


    default MotionComponent.MotionDefinition removeMotionDefinition(Object key) {
        return getMotionDefinitions().remove(key);
    }


    default void clearMotionDefinitions() {
        getMotionDefinitions().clear();
    }


    default void resetMotionComponent() {
        getMotionComponent().reset();
    }
}

