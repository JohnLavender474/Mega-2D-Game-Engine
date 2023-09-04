package com.engine.sounds

import com.badlogic.gdx.audio.Music
import com.engine.GameComponent

/**
 * A request to play a sound.
 *
 * @property source the source of the sound
 * @property loop whether to loop the sound
 */
class SoundRequest(val source: String, val loop: Boolean = false)

/**
 * A request to play a music.
 *
 * @property source the source of the music
 * @property onCompletionListener the listener to call when the music finishes
 */
class MusicRequest(val source: String, val onCompletionListener: ((Music) -> Unit)? = null)

/**
 * A component that holds a list of sounds to play and stop.
 *
 * @property playSoundRequests a list of sounds to play
 * @property stopSoundRequests a list of sounds to stop
 */
class SoundComponent : GameComponent {

  val playSoundRequests = ArrayList<SoundRequest>()
  val stopSoundRequests = ArrayList<String>()

  val playMusicRequests = ArrayList<MusicRequest>()
  val stopMusicRequests = ArrayList<String>()

  /**
   * Request to play a sound.
   *
   * @param source the source of the sound
   * @param loop whether to loop the sound
   */
  fun requestToPlaySound(source: String, loop: Boolean) =
      playSoundRequests.add(SoundRequest(source, loop))

  /**
   * Request to play a music.
   *
   * @param source the source of the music
   * @param onCompletionListener the listener to call when the music finishes playing
   */
  fun requestToPlayMusic(source: String, onCompletionListener: ((Music) -> Unit)? = null) =
      playMusicRequests.add(MusicRequest(source, onCompletionListener))

  /** Clears the list of sounds to play and stop. */
  override fun reset() {
    playSoundRequests.clear()
    stopSoundRequests.clear()
  }
}
