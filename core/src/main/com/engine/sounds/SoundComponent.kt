package com.engine.sounds

import com.engine.GameComponent

class SoundComponent : GameComponent {

  val sounds = ArrayList<SoundRequest>()
  val stop = ArrayList<String>()

  fun addSound(sound: String, loop: Boolean) {
    sounds.add(Pair(sound, loop))
  }

  fun stopSound(sound: String) {
    stop.add(sound)
  }
}
