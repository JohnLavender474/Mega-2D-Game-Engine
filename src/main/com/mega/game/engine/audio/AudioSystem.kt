package com.mega.game.engine.audio

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem
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
class AudioSystem(
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
            if (on || playSoundsWhenOff) audioComponent?.playSoundRequests?.forEach { soundRequestProcessor(it) }
            if (on || playMusicWhenOff) audioComponent?.playMusicRequests?.forEach { musicRequestProcessor(it) }
            if (on || stopSoundsWhenOff) audioComponent?.stopSoundRequests?.forEach { soundStopper(it) }
            if (on || stopMusicWhenOff) audioComponent?.stopMusicRequests?.forEach { musicStopper(it) }
            audioComponent?.reset()
        }
    }
}
