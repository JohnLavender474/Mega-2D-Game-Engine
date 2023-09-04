package com.engine.managers

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.engine.common.interfaces.Initializable
import com.engine.common.interfaces.Updatable
import java.io.File
import org.jaudiotagger.audio.AudioFileIO

/** A class that contains utilities for audio. */
object AudioUtils {

  /**
   * Gets the length of a sound in seconds.
   *
   * @param source the source of the sound file
   */
  fun getLengthOfSoundInSeconds(source: String) =
      AudioFileIO.read(File(source)).audioHeader.trackLength
}

/**
 * A class that manages audio. It can play, pause, and stop sounds and musicSuppliers. It can also
 * set the volume of sounds and music.
 *
 * @property soundSuppliers A map of sound sources to sound objects.
 * @property musicSuppliers A map of musicSuppliers sources to musicSuppliers objects.
 */
class AudioManager(
    private val soundSuppliers: HashMap<String, () -> Sound>,
    private val musicSuppliers: HashMap<String, () -> Music>
) : Initializable, Updatable {

  /**
   * The values for volume. The minimum volume is 0, the maximum volume is 10, and the default
   * volume is 5.
   */
  companion object Volume {
    private const val MIN_VOLUME = 0
    private const val MAX_VOLUME = 10
    private const val DEFAULT_VOLUME = 5
  }

  /**
   * A class that represents a sound entry. It contains the id of the sound and the source of the
   * sound. It also contains the time the sound has been playing.
   *
   * @property id The id of the sound.
   * @property source The source of the sound.
   * @property time The time the sound has been playing.
   */
  internal class SoundEntry(val id: Long, val source: String) {
    var time = 0f
  }

  private val durations = HashMap<String, Int>()
  private val playingSounds = ArrayList<SoundEntry>()
  private val playingMusic = HashSet<String>()

  private var soundVolume = DEFAULT_VOLUME
  private var musicVolume = DEFAULT_VOLUME

  /**
   * Initializes the audio manager. It sets the duration of each sound. It also sets the volume of
   * sounds and musicSuppliers.
   */
  override fun init() {
    soundSuppliers.forEach { durations[it.key] = AudioUtils.getLengthOfSoundInSeconds(it.key) }
    setSoundVolume(soundVolume)
    setMusicVolume(musicVolume)
  }

  /**
   * Updates the audio manager. It updates the time of each playing sound. If the time of a playing
   * sound is greater than or equal to the duration of the sound, the sound is removed from the list
   * of playing sounds.
   *
   * @param delta The time in seconds since the last update.
   */
  override fun update(delta: Float) {
    playingSounds.forEach {
      it.time += delta
      if (it.time >= (durations[it.source] ?: 0)) {
        playingSounds.remove(it)
      }
    }
  }

  /**
   * Sets the volume of sounds.
   *
   * @param volume The volume of sounds.
   */
  fun setSoundVolume(volume: Int) {
    soundVolume = restrictVolume(volume)
    playingSounds.forEach {
      val sound = soundSuppliers[it.source]
      sound?.invoke()?.setVolume(it.id, (soundVolume / MAX_VOLUME).toFloat())
    }
  }

  /**
   * Sets the volume of musicSuppliers.
   *
   * @param volume The volume of musicSuppliers.
   */
  fun setMusicVolume(volume: Int) {
    musicVolume = restrictVolume(volume)
    musicSuppliers.values.forEach { it.invoke().volume = (musicVolume / MAX_VOLUME).toFloat() }
  }

  private fun restrictVolume(requestedVolume: Int): Int {
    var volume = requestedVolume
    if (volume > MAX_VOLUME) {
      volume = MAX_VOLUME
    }
    if (volume < MIN_VOLUME) {
      volume = MIN_VOLUME
    }
    return volume
  }

  /**
   * Stops a sound. If the sound is not playing, it does nothing.
   *
   * @param source The source of the sound.
   */
  fun stopSound(source: String) = soundSuppliers[source]?.invoke()?.stop()

  /**
   * Pauses a sound. If the sound is not playing, it does nothing.
   *
   * @param source The source of the sound.
   */
  fun pauseSound(source: String) = soundSuppliers[source]?.invoke()?.pause()

  /**
   * Plays a sound. If the sound is already playing, it does nothing.
   *
   * @param source The source of the sound.
   */
  fun playSound(source: String, loop: Boolean) {
    val sound = soundSuppliers[source]?.invoke() ?: return
    if (loop) {
      sound.loop(soundVolume.toFloat())
    } else {
      val id = sound.play(soundVolume.toFloat())
      playingSounds.add(SoundEntry(id, source))
    }
  }

  /** Stops all sounds. If a sound is not playing, it does nothing. */
  fun stopAllSounds() = soundSuppliers.values.forEach { it.invoke().stop() }

  /** Pauses all sounds. If a sound is not playing, it does nothing. */
  fun pauseAllSounds() = soundSuppliers.values.forEach { it.invoke().pause() }

  /** Resumes all sounds. If a sound is not paused, it does nothing. */
  fun resumeAllSounds() = soundSuppliers.values.forEach { it.invoke().resume() }

  /**
   * Stops a musicSuppliers. If the musicSuppliers is not playing, it does nothing.
   *
   * @param source The source of the musicSuppliers.
   */
  fun stopMusic(source: String) {
    val music = musicSuppliers[source]?.invoke() ?: return
    music.stop()
    music.setOnCompletionListener(null)
    playingMusic.remove(source)
  }

  /**
   * Pauses a musicSuppliers. If the musicSuppliers is not playing, it does nothing.
   *
   * @param source The source of the musicSuppliers.
   */
  fun pauseMusic(source: String) = musicSuppliers[source]?.invoke()?.pause()

  /**
   * Resumes a musicSuppliers. If the musicSuppliers is not paused, it does nothing.
   *
   * @param source The source of the musicSuppliers.
   */
  fun playMusic(source: String, onCompletionListener: ((Music) -> Unit)? = null) {
    val music = musicSuppliers[source]?.invoke() ?: return
    music.volume = musicVolume.toFloat()
    music.play()
    onCompletionListener?.let { music.setOnCompletionListener(it) }
    playingMusic.add(source)
  }

  /** Stops all musicSuppliers. If a musicSuppliers is not playing, it does nothing. */
  fun stopAllMusic() {
    musicSuppliers.values.forEach {
      val music = it.invoke()
      music.stop()
      music.setOnCompletionListener(null)
    }
    playingMusic.clear()
  }

  /** Pauses all musicSuppliers. If a musicSuppliers is not playing, it does nothing. */
  fun pauseAllMusic() = musicSuppliers.values.forEach { it.invoke().pause() }
}
