package com.mega.game.engine.entities.contracts;

import com.badlogic.gdx.audio.Music;
import com.mega.game.engine.audio.AudioComponent;
import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KClass;


public interface IAudioEntity extends IGameEntity {

    
    default AudioComponent getAudioComponent() {
        KClass<AudioComponent> key = ClassInstanceUtils.convertToKClass(AudioComponent.class);
        return getComponent(key);
    }

    
    default void requestToPlaySound(Object source, boolean loop) {
        getAudioComponent().requestToPlaySound(source, loop);
    }

    
    default void requestToPlayMusic(Object source, boolean loop, Function1<? super Music, Unit> onCompletionListener) {
        getAudioComponent().requestToPlayMusic(source, loop, onCompletionListener);
    }

    
    default void stopSound(Object source) {
        getAudioComponent().getStopSoundRequests().add(source);
    }

    
    default void stopMusic(Object source) {
        getAudioComponent().getStopMusicRequests().add(source);
    }
}

