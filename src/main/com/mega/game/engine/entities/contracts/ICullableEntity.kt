package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.cullables.CullablesComponent;
import com.mega.game.engine.cullables.ICullable;
import kotlin.reflect.KClass;


public interface ICullableEntity extends IGameEntity {


    default CullablesComponent getCullablesComponent() {
        KClass<CullablesComponent> key = ClassInstanceUtils.convertToKClass(CullablesComponent.class);
        return getComponent(key);
    }


    default void putCullable(String key, ICullable cullable) {
        getCullablesComponent().put(key, cullable);
    }


    default void removeCullable(String key) {
        getCullablesComponent().remove(key);
    }
}

