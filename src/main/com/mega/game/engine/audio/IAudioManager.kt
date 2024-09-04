package com.mega.game.engine.audio

/** The audio manager. */
interface IAudioManager {

    var soundVolume: Float
    var musicVolume: Float

    /**
     * Plays music with the given [key].
     *
     * @param key the key of the music
     */
    fun playMusic(key: Any? = null, loop: Boolean = false)

    /**
     * Stops the music with the given [key].
     *
     * @param key the key of the music
     */
    fun stopMusic(key: Any? = null)

    /**
     * Pauses the music with the given [key].
     *
     * @param key the key of the music
     */
    fun pauseMusic(key: Any? = null)

    /**
     * Plays the sound with the given [key] and [loop] value. If [loop] is true, the sound will loop.
     *
     * @param key the key of the sound
     * @param loop whether to loop the sound
     */
    fun playSound(key: Any? = null, loop: Boolean = false)

    /**
     * Stops the sound with the given [key].
     *
     * @param key the key of the sound
     */
    fun stopSound(key: Any? = null)

    /**
     * Pauses the sound with the given [key].
     *
     * @param key the key of the sound
     */
    fun pauseSound(key: Any? = null)
}
