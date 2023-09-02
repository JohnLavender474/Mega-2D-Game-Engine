package com.engine.sounds

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.engine.GameEntity
import com.engine.GameSystem
import com.engine.common.objects.ImmutableCollection

class SoundSystem(private val assetManager: AssetManager) : GameSystem(SoundComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float) {
    if (!on) {
      return
    }
    entities.forEach { entity ->
      val soundComponent = entity.getComponent(SoundComponent::class)
      soundComponent?.sounds?.forEach { sound ->
        if (sound.second) {
          assetManager.get(sound.first, Sound::class.java).loop()
        } else {
          assetManager.get(sound.first, Sound::class.java).play()
        }
      }
      soundComponent?.stop?.forEach { sound -> assetManager.get(sound, Sound::class.java).stop() }
    }
  }
}
