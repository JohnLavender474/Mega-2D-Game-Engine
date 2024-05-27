package com.engine.entities.contracts;

import com.badlogic.gdx.utils.OrderedMap;
import com.engine.common.ClassInstanceUtils;
import com.engine.entities.IGameEntity;
import com.engine.motion.IMotion;
import com.engine.motion.MotionComponent;
import kotlin.reflect.KClass;

/**
 * An entity that can be moved by a motion value. The motion value is obtained from an IMotion
 * that is stored in a MotionComponent.
 */
public interface IMotionEntity extends IGameEntity {

    /**
     * Returns the MotionComponent of this entity. Throws exception if no MotionComponent has been
     * added.
     *
     * @return the MotionComponent of this entity
     */
    default MotionComponent getMotionComponent() {
        KClass<MotionComponent> key = ClassInstanceUtils.convertToKClass(MotionComponent.class);
        return getComponent(key);
    }

    /**
     * Returns the motions of this entity.
     *
     * @return the motions of this entity
     */
    default OrderedMap<Object, MotionComponent.MotionDefinition> getMotionDefinitions() {
        return getMotionComponent().getDefinitions();
    }

    /**
     * Returns the IMotion associated with the given key.
     *
     * @param key the key to get the IMotion for
     * @return the IMotion associated with the given key
     */
    default MotionComponent.MotionDefinition getMotionDefinition(Object key) {
        return getMotionDefinitions().get(key);
    }

    /**
     * Returns the IMotion associated with the given key.
     *
     * @param key the key to get the IMotion for
     * @return the IMotion associated with the given key
     */
    default IMotion getMotion(Object key) {
        MotionComponent.MotionDefinition definition = getMotionDefinition(key);
        return definition != null ? definition.getMotion() : null;
    }

    /**
     * Adds an IMotion to this entity.
     *
     * @param key        the key to associate with the IMotion
     * @param definition the IMotion and function pair
     * @return the prior IMotion value if any, or null
     */
    default MotionComponent.MotionDefinition putMotionDefinition(Object key,
                                                                 MotionComponent.MotionDefinition definition) {
        return getMotionComponent().put(key, definition);
    }

    /**
     * Removes an IMotion from this entity.
     *
     * @param key the key to remove
     * @return the IMotion that was removed, or null
     */
    default MotionComponent.MotionDefinition removeMotionDefinition(Object key) {
        return getMotionDefinitions().remove(key);
    }

    /**
     * Clears all motions from this entity.
     */
    default void clearMotionDefinitions() {
        getMotionDefinitions().clear();
    }

    /**
     * Resets the motions in this entity.
     */
    default void resetMotionComponent() {
        getMotionComponent().reset();
    }
}

