package com.engine.audio

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.GameEntity
import com.engine.systems.GameSystem

/**
 * A [GameSystem] that processes [SoundComponent]s. It plays and stops sounds and music.
 *
 * @property assetManager the [AssetManager]
 * @property playSoundsWhenOff whether to play sounds when the system is off
 * @property playMusicWhenOff whether to play music when the system is off
 * @property stopSoundsWhenOff whether to stop sounds when the system is off
 * @property stopMusicWhenOff whether to stop music when the system is off
 */
class AudioSystem(
    private val assetManager: AssetManager,
    var playSoundsWhenOff: Boolean = false,
    var playMusicWhenOff: Boolean = false,
    var stopSoundsWhenOff: Boolean = true,
    var stopMusicWhenOff: Boolean = true
) : GameSystem(SoundComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    entities.forEach { entity ->
      val soundComponent = entity.getComponent(SoundComponent::class)

      // play sounds
      if (on || playSoundsWhenOff) {
        soundComponent?.playSoundRequests?.forEach {
          val sound = assetManager.get(it.source, Sound::class.java)
          if (it.loop) {
            sound.loop()
          } else {
            sound.play()
          }
        }
      }

      // play music
      if (on || playMusicWhenOff) {
        soundComponent?.playMusicRequests?.forEach {
          val music = assetManager.get(it.source, Music::class.java)
          it.onCompletionListener?.let { listener -> music.setOnCompletionListener(listener) }
          music.play()
        }
      }

      // stop sounds
      if (on || stopSoundsWhenOff) {
        soundComponent?.stopSoundRequests?.forEach {
          assetManager.get(it, Sound::class.java).stop()
        }
      }

      // stop music
      if (on || stopMusicWhenOff) {
        soundComponent?.stopMusicRequests?.forEach {
          assetManager.get(it, Music::class.java).stop()
        }
      }
    }
  }
}
