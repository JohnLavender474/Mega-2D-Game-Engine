package com.mega.game.engine.entities.contracts;

import com.badlogic.gdx.utils.ObjectMap;
import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.common.interfaces.UpdateFunction;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.drawables.sprites.GameSprite;
import com.mega.game.engine.drawables.sprites.SpritesComponent;
import kotlin.reflect.KClass;


public interface ISpritesEntity extends IGameEntity {

    
    default SpritesComponent getSpritesComponent() {
        KClass<SpritesComponent> key = ClassInstanceUtils.convertToKClass(SpritesComponent.class);
        return getComponent(key);
    }

    
    default ObjectMap<String, GameSprite> getSprites() {
        return getSpritesComponent().getSprites();
    }

    
    default GameSprite getFirstSprite() {
        return getSprites().iterator().next().value;
    }

    
    default void putUpdateFunction(String key, UpdateFunction<GameSprite> updateFunction) {
        getSpritesComponent().putUpdateFunction(key, updateFunction);
    }
}
