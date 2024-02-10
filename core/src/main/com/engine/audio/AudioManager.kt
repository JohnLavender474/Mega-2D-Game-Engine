package com.engine.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.engine.common.GameLogger

object AudioManager : IAudioManager {

    private const val TAG = "AudioManager"
    private const val MIN_VOLUME = 0f
    private const val MAX_VOLUME = 10f
    private const val DEFAULT_VOLUME = 5f

    override var soundVolume = DEFAULT_VOLUME
        set(value) {
            field = restrictVolume(value)
            GameLogger.debug(TAG, "setSoundVolume(): Sound volume set to: $soundVolume")
        }
        get() = field / MAX_VOLUME

    override var musicVolume = DEFAULT_VOLUME
        set(value) {
            field = restrictVolume(value)
            GameLogger.debug(TAG, "setMusicVolume(): Music volume set to: $musicVolume")
        }
        get() = field / MAX_VOLUME

    /**
     * Plays music with the given key and loop settings.
     *
     * @param key the music to play
     * @param loop whether to loop the music
     */
    override fun playMusic(key: Any?, loop: Boolean) {
        if (key is Music) {
            key.volume = musicVolume
            key.isLooping = loop
            key.play()
            GameLogger.debug(TAG, "playMusic(): Playing music with key: $key, loop: $loop")
        }
    }

    /**
     * Stops the music with the given key.
     *
     * @param key the music to stop
     */
    override fun stopMusic(key: Any?) {
        if (key is Music) {
            key.stop()
            GameLogger.debug(TAG, "stopMusic(): Stopping music with key: $key")
        }
    }

    /**
     * Pauses the music with the given key.
     *
     * @param key the music to pause
     */
    override fun pauseMusic(key: Any?) {
        if (key is Music) {
            key.pause()
            GameLogger.debug(TAG, "pauseMusic(): Pausing music with key: $key")
        }
    }

    /**
     * Plays the sound with the given key and loop settings.
     *
     * @param key the sound to play
     * @param loop whether to loop the sound
     */
    override fun playSound(key: Any?, loop: Boolean) {
        if (key is Sound) {
            if (loop) {
                key.loop(soundVolume)
                GameLogger.debug(TAG, "playSound(): Playing sound with key: $key, loop: $loop")
            } else {
                key.play(soundVolume)
                GameLogger.debug(TAG, "playSound(): Playing sound with key: $key, loop: $loop")
            }
        }
    }

    /**
     * Stops the sound with the given key.
     *
     * @param key the sound to stop
     */
    override fun stopSound(key: Any?) {
        if (key is Sound) {
            key.stop()
            GameLogger.debug(TAG, "stopSound(): Stopping sound with key: $key")
        }
    }

    /**
     * Pauses the sound with the given key.
     *
     * @param key the sound to pause
     */
    override fun pauseSound(key: Any?) {
        if (key is Sound) {
            key.pause()
            GameLogger.debug(TAG, "pauseSound(): Pausing sound with key: $key")
        }
    }

    private fun restrictVolume(requestedVolume: Float): Float {
        var volume = requestedVolume
        if (volume > MAX_VOLUME) volume = MAX_VOLUME
        if (volume < MIN_VOLUME) volume = MIN_VOLUME
        return volume
    }
}
