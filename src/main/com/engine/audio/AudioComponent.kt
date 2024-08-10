package com.engine.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent

/**
 * A request to play a sound.
 *
 * @property source the source of the sound
 * @property loop whether to loop the sound
 */
data class SoundRequest(val source: Any, val loop: Boolean = false)

/**
 * A request to play a music.
 *
 * @property source the source of the music
 * @property onCompletionListener the listener to call when the music finishes
 */
data class MusicRequest(
    val source: Any, val loop: Boolean = true, val onCompletionListener: ((Music) -> Unit)? = null
)

/**
 * A component that holds a list of sounds to play and stop.
 *
 * @property playSoundRequests a list of sounds to play
 * @property stopSoundRequests a list of sounds to stop
 */
class AudioComponent() : IGameComponent {

    val playSoundRequests = Array<SoundRequest>()
    val stopSoundRequests = Array<Any>()

    val playMusicRequests = Array<MusicRequest>()
    val stopMusicRequests = Array<Any>()

    /**
     * Request to play a sound.
     *
     * @param source the source of the sound
     * @param loop whether to loop the sound
     */
    fun requestToPlaySound(source: Any, loop: Boolean) = playSoundRequests.add(SoundRequest(source, loop))

    /**
     * Request to play a music.
     *
     * @param source the source of the music
     * @param onCompletionListener the listener to call when the music finishes playing
     */
    fun requestToPlayMusic(
        source: Any, loop: Boolean = true, onCompletionListener: ((Music) -> Unit)? = null
    ) = playMusicRequests.add(MusicRequest(source, loop, onCompletionListener))

    /**
     * Request to play a music using a runnable.
     *
     * @param source the source of the music
     * @param loop whether to loop the music
     * @param onCompletionListener the listener to call when the music finishes playing
     */
    fun requestToPlayMusic(
        source: Any, loop: Boolean = true, onCompletionListener: Runnable? = null
    ) = playMusicRequests.add(MusicRequest(source, loop) { onCompletionListener?.run() })

    /** Clears the list of sounds to play and stop. */
    override fun reset() {
        playSoundRequests.clear()
        stopSoundRequests.clear()
        playMusicRequests.clear()
        stopMusicRequests.clear()
    }
}
