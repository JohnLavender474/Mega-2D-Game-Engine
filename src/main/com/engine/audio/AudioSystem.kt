package com.engine.audio

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem
import java.util.function.Consumer

/**
 * A [GameSystem] that processes [AudioComponent]s. It handles sounds and music requested to be
 * played or stopped by entities.
 *
 * @property soundRequestProcessor the processor for sound requests
 * @property musicRequestProcessor the processor for music requests
 * @property soundStopper the stopper for sounds
 * @property musicStopper the stopper for music
 * @property playSoundsWhenOff whether to play sounds when the system is off
 * @property playMusicWhenOff whether to play music when the system is off
 * @property stopSoundsWhenOff whether to stop sounds when the system is off
 * @property stopMusicWhenOff whether to stop music when the system is off
 */
open class AudioSystem(
    private val soundRequestProcessor: (SoundRequest) -> Unit,
    private val musicRequestProcessor: (MusicRequest) -> Unit,
    private val soundStopper: (Any) -> Unit,
    private val musicStopper: (Any) -> Unit,
    var playSoundsWhenOff: Boolean = false,
    var playMusicWhenOff: Boolean = false,
    var stopSoundsWhenOff: Boolean = true,
    var stopMusicWhenOff: Boolean = true
) : GameSystem(AudioComponent::class) {

    /**
     * Convenience constructor that takes [Consumer]s for sound and music requests and stoppers.
     */
    constructor(
        soundRequestProcessor: Consumer<SoundRequest>,
        musicRequestProcessor: Consumer<MusicRequest>,
        soundStopper: Consumer<Any>,
        musicStopper: Consumer<Any>,
        playSoundsWhenOff: Boolean = false,
        playMusicWhenOff: Boolean = false,
        stopSoundsWhenOff: Boolean = true,
        stopMusicWhenOff: Boolean = true
    ) : this(
        { soundRequestProcessor.accept(it) },
        { musicRequestProcessor.accept(it) },
        { soundStopper.accept(it) },
        { musicStopper.accept(it) },
        playSoundsWhenOff,
        playMusicWhenOff,
        stopSoundsWhenOff,
        stopMusicWhenOff
    )

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        entities.forEach { entity ->
            val audioComponent = entity.getComponent(AudioComponent::class)

            // play sounds
            if (on || playSoundsWhenOff) {
                audioComponent?.playSoundRequests?.forEach {
                    soundRequestProcessor(it)
                    /*
                    val sound = assetManager.get(it.source, Sound::class.java)
                    audioManager.playSound(sound, it.loop)
                     */
                }
            }

            // play music
            if (on || playMusicWhenOff) {
                audioComponent?.playMusicRequests?.forEach {
                    musicRequestProcessor(it)
                    /*
                    val music = assetManager.get(it.source, Music::class.java)
                    it.onCompletionListener?.let { listener -> music.setOnCompletionListener(listener) }
                    audioManager.playMusic(music, it.loop)
                     */
                }
            }

            // stop sounds
            if (on || stopSoundsWhenOff)
                audioComponent?.stopSoundRequests?.forEach {
                    soundStopper(it)
                    /*
                    val sound = assetManager.get(it, Sound::class.java)
                    audioManager.stopSound(sound)
                     */
                }

            // stop music
            if (on || stopMusicWhenOff)
                audioComponent?.stopMusicRequests?.forEach {
                    musicStopper(it)
                    /*
                    val music = assetManager.get(it, Music::class.java)
                    audioManager.stopMusic(music)
                     */
                }

            audioComponent?.reset()
        }
    }
}
