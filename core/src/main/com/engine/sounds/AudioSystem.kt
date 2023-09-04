package com.engine.sounds

import com.engine.GameEntity
import com.engine.GameSystem
import com.engine.common.objects.ImmutableCollection
import com.engine.managers.AudioManager

/**
 * A [GameSystem] that processes [SoundComponent]s. It plays and stops sounds and music.
 *
 * @property audioManager the [AudioManager] to use
 * @property playSoundsWhenOff whether to play sounds when the system is off
 * @property playMusicWhenOff whether to play music when the system is off
 * @property stopSoundsWhenOff whether to stop sounds when the system is off
 * @property stopMusicWhenOff whether to stop music when the system is off
 */
class AudioSystem(
    private val audioManager: AudioManager,
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
        soundComponent?.playSoundRequests?.forEach { audioManager.playSound(it.source, it.loop) }
      }

      // play music
      if (on || playMusicWhenOff) {
        soundComponent?.playMusicRequests?.forEach {
          audioManager.playMusic(it.source, it.onCompletionListener)
        }
      }

      // stop sounds
      if (on || stopSoundsWhenOff) {
        soundComponent?.stopSoundRequests?.forEach { audioManager.stopSound(it) }
      }

      // stop music
      if (on || stopMusicWhenOff) {
        soundComponent?.stopMusicRequests?.forEach { audioManager.stopMusic(it) }
      }
    }
  }
}
