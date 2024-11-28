package com.mega.game.engine.entities.contracts;

import com.badlogic.gdx.utils.OrderedMap;
import com.mega.game.engine.behaviors.BehaviorsComponent;
import com.mega.game.engine.behaviors.IBehavior;
import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import kotlin.jvm.functions.Function2;
import kotlin.reflect.KClass;

import java.util.stream.StreamSupport;


public interface IBehaviorsEntity extends IGameEntity {

    
    default BehaviorsComponent getBehaviorsComponent() {
        KClass<BehaviorsComponent> key = ClassInstanceUtils.convertToKClass(BehaviorsComponent.class);
        return getComponent(key);
    }

    
    default boolean isBehaviorActive(Object key) {
        return getBehaviorsComponent().isBehaviorActive(key);
    }

    
    default OrderedMap<Object, IBehavior> getBehaviors() {
        return getBehaviorsComponent().getBehaviors();
    }

    
    default IBehavior getBehavior(Object key) {
        return getBehaviors().get(key);
    }

    
    default boolean isAnyBehaviorActive(Object... keys) {
        return isAnyBehaviorActive(java.util.Arrays.asList(keys));
    }

    
    default boolean isAnyBehaviorActive(Iterable<Object> keys) {
        return StreamSupport.stream(keys.spliterator(), false)
                .anyMatch(this::isBehaviorActive);
    }

    
    default boolean areAllBehaviorsActive(Object... keys) {
        return areAllBehaviorsActive(java.util.Arrays.asList(keys));
    }

    
    default boolean areAllBehaviorsActive(Iterable<Object> keys) {
        return StreamSupport.stream(keys.spliterator(), false)
                .allMatch(this::isBehaviorActive);
    }

    
    default void resetBehavior(Object key) {
        IBehavior behavior = getBehavior(key);
        if (behavior != null) {
            behavior.reset();
        }
    }

    
    default boolean isBehaviorAllowed(Object key) {
        return getBehaviorsComponent().isBehaviorAllowed(key);
    }

    
    default void setBehaviorAllowed(Object key, boolean allowed) {
        getBehaviorsComponent().setBehaviorAllowed(key, allowed);
    }

    
    default void setBehaviorsAllowed(Function2<Object, IBehavior, Boolean> function) {
        getBehaviorsComponent().setBehaviorsAllowed(function);
    }

    
    default void setBehaviorsAllowed(Iterable<Object> keys, boolean allowed) {
        getBehaviorsComponent().setBehaviorsAllowed(keys, allowed);
    }

    
    default void setAllBehaviorsAllowed(boolean allowed) {
        getBehaviorsComponent().setAllBehaviorsAllowed(allowed);
    }
}

