package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.cullables.CullablesComponent;
import com.mega.game.engine.cullables.ICullable;
import kotlin.reflect.KClass;

/**
 * An GameEntity that can be culled.
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

