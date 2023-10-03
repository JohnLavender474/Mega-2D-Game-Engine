package com.engine.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import org.jaudiotagger.audio.AudioFileIO

/** A class that contains utilities for audio. */
object AudioUtils {

  /**
   * Gets the length of a sound in seconds.
   *
   * @param source the source of the sound file
   */
  fun getLengthOfSoundInSeconds(source: String) =
      AudioFileIO.read(Gdx.files.internal(source).file()).audioHeader.trackLength
}

/**
 * A class that manages audio. It can play, pause, and stop sounds and musicSuppliers. It can also
 * set the volume of sounds and music.
 */
class AudioManager(private val assetManager: AssetManager) : IAudioManager {

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
   * @param id The id of the sound.
   * @param time The total time the sound should play for
   * @property delta The time the sound has been playing for
   */
  internal class SoundEntry(val id: Long, val sound: Sound, val time: Int = 0) {
    var delta = 0f
  }

  private val durations = ObjectMap<String, Int>()
  private val loopingSounds = Array<SoundEntry>()
  private val playingSounds = Array<SoundEntry>()
  private val playingMusic = ObjectSet<String>()

  private var soundVolume = DEFAULT_VOLUME
  private var musicVolume = DEFAULT_VOLUME

  override fun update(delta: Float) {
    val iter = playingSounds.iterator()

    while (iter.hasNext()) {
      val soundEntry = iter.next()
      soundEntry.delta += delta

      if (soundEntry.time >= soundEntry.delta) {
        soundEntry.sound.stop(soundEntry.id)
        iter.remove()
      }
    }
  }

  override fun setSoundVolume(volume: Int) {
    soundVolume = restrictVolume(volume)
  }

  override fun setMusicVolume(volume: Int) {
    musicVolume = restrictVolume(volume)
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

  override fun stopSound(source: String) = assetManager.get(source, Sound::class.java).stop()

  override fun pauseSound(source: String) = assetManager.get(source, Sound::class.java).pause()

  override fun playSound(source: String, loop: Boolean) {
    try {
      val sound = assetManager.get(source, Sound::class.java)

      if (!durations.containsKey(source)) {
        val time = AudioUtils.getLengthOfSoundInSeconds(source)
        durations.put(source, time)
      }
      val time = durations.get(source)

      if (loop) {
        val id = sound.loop(soundVolume.toFloat())
        loopingSounds.add(SoundEntry(id, sound))
      } else {
        val id = sound.play(soundVolume.toFloat())
        playingSounds.add(SoundEntry(id, sound, time))
      }
    } catch (e: Exception) {
      e.printStackTrace()
      Gdx.app.log("Audio Manager", "Error playing sound: $source", e)
    }
  }

  override fun stopAllLoopingSounds() {
    loopingSounds.forEach { it.sound.stop(it.id) }
    loopingSounds.clear()
  }

  override fun stopAllPlayingSounds() {
    playingSounds.forEach { it.sound.stop(it.id) }
    playingSounds.clear()
  }

  override fun stopAllSounds() =
      assetManager.getAll(Sound::class.java, Array()).forEach { it.stop() }

  override fun pauseAllSounds() =
      assetManager.getAll(Sound::class.java, Array()).forEach { it.pause() }

  override fun resumeAllSounds() =
      assetManager.getAll(Sound::class.java, Array()).forEach { it.resume() }

  override fun stopMusic(source: String) {
    val music = assetManager.get(source, Music::class.java)
    music.stop()
    music.setOnCompletionListener(null)
    playingMusic.remove(source)
  }

  override fun pauseMusic(source: String) = assetManager.get(source, Music::class.java).pause()

  override fun playMusic(source: String, onCompletionListener: ((Music) -> Unit)?) {
    val music = assetManager.get(source, Music::class.java)
    music.volume = musicVolume.toFloat()
    music.play()
    onCompletionListener?.let { music.setOnCompletionListener(it) }
    playingMusic.add(source)
  }

  override fun stopAllMusic() {
    assetManager.getAll(Music::class.java, Array()).forEach {
      val music = it
      music.stop()
      music.setOnCompletionListener(null)
    }
    playingMusic.clear()
  }

  override fun pauseAllMusic() =
      assetManager.getAll(Music::class.java, Array()).forEach { it.pause() }
}
