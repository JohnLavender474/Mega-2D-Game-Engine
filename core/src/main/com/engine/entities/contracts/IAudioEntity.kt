package com.engine.entities.contracts

import com.badlogic.gdx.audio.Music
import com.engine.audio.AudioComponent
import com.engine.entities.IGameEntity

/** An entity containing audio. */
interface IAudioEntity : IGameEntity {

  /**
   * Gets the [AudioComponent] of this entity.
   *
   * @return the [AudioComponent] of this entity
   */
  fun getAudioComponent() = getComponent(AudioComponent::class)!!

  /**
   * Request to play a sound.
   *
   * @param source the source of the sound
   * @param loop whether to loop the sound
   */
  fun requestToPlaySound(source: String, loop: Boolean) =
      getAudioComponent().requestToPlaySound(source, loop)

  /**
   * Request to play a music.
   *
   * @param source the source of the music
   * @param onCompletionListener the listener to call when the music finishes playing
   */
  fun requestToPlayMusic(source: String, onCompletionListener: ((Music) -> Unit)? = null) =
      getAudioComponent().requestToPlayMusic(source, onCompletionListener)

  /**
   * Stops the sound with the given source.
   *
   * @param source the source of the sound
   */
  fun stopSound(source: String) = getAudioComponent().stopSoundRequests.add(source)

  /**
   * Stops the music with the given source.
   *
   * @param source the source of the music
   */
  fun stopMusic(source: String) = getAudioComponent().stopMusicRequests.add(source)
}
