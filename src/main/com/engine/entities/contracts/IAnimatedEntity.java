package com.engine.entities.contracts;

import com.badlogic.gdx.utils.Array;
import com.engine.animations.AnimationsComponent;
import com.engine.animations.IAnimator;
import com.engine.common.ClassInstanceUtils;
import com.engine.drawables.sprites.GameSprite;
import com.engine.entities.IGameEntity;
import kotlin.Pair;
import kotlin.jvm.functions.Function0;
import kotlin.reflect.KClass;

/**
 * Interface for entities that have animations.
 */
public interface IAnimatedEntity extends IGameEntity {

    default AnimationsComponent getAnimationsComponent() {
        KClass<AnimationsComponent> key = ClassInstanceUtils.convertToKClass(AnimationsComponent.class);
        return getComponent(key);
    }

    default Array<Pair<Function0<GameSprite>, IAnimator>> getAnimators() {
        return getAnimationsComponent().getAnimators();
    }
}
