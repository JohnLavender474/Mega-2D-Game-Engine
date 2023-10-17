package com.engine.audio

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A [GameSystem] that processes [AudioComponent]s. It plays and stops sounds and music.
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
) : GameSystem(AudioComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    entities.forEach { entity ->
      val audioComponent = entity.getComponent(AudioComponent::class)

      // play sounds
      if (on || playSoundsWhenOff) {
        audioComponent?.playSoundRequests?.forEach {
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
        audioComponent?.playMusicRequests?.forEach {
          val music = assetManager.get(it.source, Music::class.java)
          it.onCompletionListener?.let { listener -> music.setOnCompletionListener(listener) }
          music.play()
        }
      }

      // stop sounds
      if (on || stopSoundsWhenOff) {
        audioComponent?.stopSoundRequests?.forEach {
          assetManager.get(it, Sound::class.java).stop()
        }
      }

      // stop music
      if (on || stopMusicWhenOff) {
        audioComponent?.stopMusicRequests?.forEach {
          assetManager.get(it, Music::class.java).stop()
        }
      }
    }
  }
}
