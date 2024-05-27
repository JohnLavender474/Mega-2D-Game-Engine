package com.engine.entities.contracts;

import com.badlogic.gdx.audio.Music;
import com.engine.audio.AudioComponent;
import com.engine.common.ClassInstanceUtils;
import com.engine.entities.IGameEntity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KClass;

/**
 * An entity containing audio.
 */
public interface IAudioEntity extends IGameEntity {

    /**
     * Gets the AudioComponent of this entity.
     *
     * @return the AudioComponent of this entity
     */
    default AudioComponent getAudioComponent() {
        KClass<AudioComponent> key = ClassInstanceUtils.convertToKClass(AudioComponent.class);
        return getComponent(key);
    }

    /**
     * Request to play a sound.
     *
     * @param source the source of the sound
     * @param loop   whether to loop the sound
     */
    default void requestToPlaySound(Object source, boolean loop) {
        getAudioComponent().requestToPlaySound(source, loop);
    }

    /**
     * Request to play music.
     *
     * @param source               the source of the music
     * @param loop                 whether to loop the music
     * @param onCompletionListener the listener to call when the music finishes playing
     */
    default void requestToPlayMusic(Object source, boolean loop, Function1<? super Music, Unit> onCompletionListener) {
        getAudioComponent().requestToPlayMusic(source, loop, onCompletionListener);
    }

    /**
     * Stops the sound with the given source.
     *
     * @param source the source of the sound
     */
    default void stopSound(Object source) {
        getAudioComponent().getStopSoundRequests().add(source);
    }

    /**
     * Stops the music with the given source.
     *
     * @param source the source of the music
     */
    default void stopMusic(Object source) {
        getAudioComponent().getStopMusicRequests().add(source);
    }
}

