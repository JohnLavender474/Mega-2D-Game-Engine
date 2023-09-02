package com.engine.sounds

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.interfaces.Updatable

class SoundManager(
    private val assetManager: AssetManager,
    private val sounds: OrderedMap<String, Sound>
) : Updatable {

  companion object {
    private const val MIN_VOLUME = 0
    private const val MAX_VOLUME = 10
    private const val DEFAULT_VOLUME = 5
  }

  private data class SoundEntry(val id: Long, val sound: Sound, var time: Float)

  private val playingSounds = ArrayList<SoundEntry>()

  private var soundVolume = DEFAULT_VOLUME
  private var musicVolume = DEFAULT_VOLUME
  private var currMusic: Music? = null

  override fun update(delta: Float) {
    val eIter = playingSounds.iterator()
    while (eIter.hasNext()) {
      val e = eIter.next()
      e.time += delta
      if (e.time > e.sound.getSeconds()) {
        eIter.remove()
      }
    }
  }

  fun setVolume(newVolume: Int) {
    soundVolume = newVolume
    if (soundVolume > MAX_VOLUME) {
      soundVolume = MAX_VOLUME
    }
    if (soundVolume < MIN_VOLUME) {
      soundVolume = MIN_VOLUME
    }
    for (e in playingSounds!!) {
      val s = sounds!!.get<String>(e.ass)
      s.setVolume(e.id, soundVolume.toFloat() / MAX_VOLUME)
    }
  }

  fun stop(ass: String) = sounds.get(ass).stop()

  fun pause() = sounds.values().forEach { it.pause() }

  fun resume() = sounds.values().forEach { it.resume() }
}
