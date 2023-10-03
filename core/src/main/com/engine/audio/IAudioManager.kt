package com.engine.audio

import com.badlogic.gdx.audio.Music
import com.engine.common.interfaces.Updatable

interface IAudioManager : Updatable {

  override fun update(delta: Float) {}

  /**
   * Sets the volume of sounds.
   *
   * @param volume The volume of sounds.
   */
  fun setSoundVolume(volume: Int)

  /**
   * Sets the volume of music.
   *
   * @param volume The volume of musicSuppliers.
   */
  fun setMusicVolume(volume: Int)

  /**
   * Stops a sound.
   *
   * @param source The source of the sound.
   */
  fun stopSound(source: String)

  /**
   * Pauses a sound.
   *
   * @param source The source of the sound.
   */
  fun pauseSound(source: String)

  /**
   * Plays a sound.
   *
   * @param source The source of the sound.
   */
  fun playSound(source: String, loop: Boolean)

  /** Stops all looping sounds. */
  fun stopAllLoopingSounds()

  /** Stops all playing sounds but NONE of the looping sounds. */
  fun stopAllPlayingSounds()

  /** Stops all sounds, both those looping and non-looping. */
  fun stopAllSounds()

  /** Pauses all sounds. If a sound is not playing, it does nothing. */
  fun pauseAllSounds()

  /** Resumes all sounds. If a sound is not paused, it does nothing. */
  fun resumeAllSounds()

  /**
   * Stops a music.
   *
   * @param source The source of the musicSuppliers.
   */
  fun stopMusic(source: String)

  /**
   * Pauses a music.
   *
   * @param source The source of the musicSuppliers.
   */
  fun pauseMusic(source: String)

  /**
   * Resumes a music.
   *
   * @param source The source of the musicSuppliers.
   */
  fun playMusic(source: String, onCompletionListener: ((Music) -> Unit)? = null)

  /** Stops all music. */
  fun stopAllMusic()

  /** Pauses all music. */
  fun pauseAllMusic()
}
