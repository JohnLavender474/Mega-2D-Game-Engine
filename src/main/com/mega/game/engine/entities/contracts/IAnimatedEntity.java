package com.mega.game.engine.entities.contracts;

import com.badlogic.gdx.utils.Array;
import com.mega.game.engine.animations.AnimationsComponent;
import com.mega.game.engine.animations.IAnimator;
import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.common.objects.GamePair;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.drawables.sprites.GameSprite;
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

    default Array<GamePair<Function0<GameSprite>, IAnimator>> getAnimators() {
        return getAnimationsComponent().getAnimators();
    }
}
