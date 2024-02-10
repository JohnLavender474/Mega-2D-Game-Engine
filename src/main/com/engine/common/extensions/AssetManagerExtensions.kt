package com.engine.common.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Queue

/**
 * Loads all assets in a directory.
 *
 * @param directory The directory to load assets from.
 * @param type The type of the assets
 */
fun <T> AssetManager.loadAssetsInDirectory(
    directory: String,
    type: Class<T>,
) {
    val queue = Queue<String>()
    queue.addLast(directory)

    while (!queue.isEmpty) {
        val path = queue.removeFirst()
        val file = Gdx.files.internal(path)

        if (file.isDirectory) {
            file.list().forEach { queue.addLast(it.path()) }
        } else {
            load(file.path(), type)
        }
    }
}

/**
 * Gets the texture atlas for the specified atlas.
 *
 * @param atlas The atlas to get.
 * @return The texture atlas.
 */
fun AssetManager.getTextureAtlas(atlas: String): TextureAtlas {
    return get(atlas, TextureAtlas::class.java)
}

/**
 * Gets the texture region for the specified atlas and region.
 *
 * @param atlas The atlas to get.
 * @param region The region to get.
 * @return The texture region.
 */
fun AssetManager.getTextureRegion(atlas: String, region: String): TextureRegion {
    return getTextureAtlas(atlas).findRegion(region)
}

/**
 * Gets the sound for the specified sound.
 *
 * @param sound The sound to get.
 * @return The sound.
 */
fun AssetManager.getSound(sound: String): Sound {
    return get(sound, Sound::class.java)
}

/**
 * Gets the music for the specified music.
 *
 * @param music The music to get.
 * @return The music.
 */
fun AssetManager.getMusic(music: String): Music {
    return get(music, Music::class.java)
}
