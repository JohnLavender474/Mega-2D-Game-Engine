package com.engine.sounds

import java.io.File
import org.jaudiotagger.audio.AudioFileIO

fun getLengthOfSoundInSeconds(soundSrc: String) =
    AudioFileIO.read(File(soundSrc)).audioHeader.trackLength
