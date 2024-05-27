package com.engine.entities.contracts;

import com.engine.common.ClassInstanceUtils;
import com.engine.cullables.CullablesComponent;
import com.engine.cullables.ICullable;
import com.engine.entities.IGameEntity;
import kotlin.reflect.KClass;

/**
 * An IGameEntity that can be culled.
 */
public interface ICullableEntity extends IGameEntity {

    /**
     * Returns the CullablesComponent of this entity.
     *
     * @return the CullablesComponent of this entity
     */
    default CullablesComponent getCullablesComponent() {
        KClass<CullablesComponent> key = ClassInstanceUtils.convertToKClass(CullablesComponent.class);
        return getComponent(key);
    }

    /**
     * Adds an ICullable to the list of cullables.
     *
     * @param key      The key of the cullable.
     * @param cullable The cullable to add.
     */
    default void putCullable(String key, ICullable cullable) {
        getCullablesComponent().put(key, cullable);
    }

    /**
     * Removes an ICullable from the list of cullables.
     *
     * @param key The key of the cullable to remove.
     */
    default void removeCullable(String key) {
        getCullablesComponent().remove(key);
    }
}

