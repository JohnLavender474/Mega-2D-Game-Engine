package com.engine.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectMap
import kotlin.reflect.KClass

/**
 * A manager for loading and fetching game assets. Asset types must be compatible with
 * [AssetManager].
 *
 * @property musicEntries the entries for music assets
 * @property soundEntries the entries for sound assets
 * @property textureAtlasEntries the entries for texture atlas assets
 * @property otherEntries the entries for any other types of assets
 */
class GameAssetManager(
    private val musicEntries: Array<String>,
    private val soundEntries: Array<String>,
    private val textureAtlasEntries: Array<String>,
    private val otherEntries: ObjectMap<Class<*>, Array<String>>? = null,
    private val assetManager: AssetManager = AssetManager(),
) : Disposable {

  /**
   * Loads the assets. Must be called before any assets can be used.
   *
   * @see [AssetManager.load] and [AssetManager.finishLoading]
   */
  fun loadAssets() {
    musicEntries.forEach { assetManager.load(it, Music::class.java) }
    soundEntries.forEach { assetManager.load(it, Sound::class.java) }
    textureAtlasEntries.forEach { assetManager.load(it, TextureAtlas::class.java) }
    otherEntries?.entries()?.forEach { e ->
      val type = e.key
      val entries = e.value
      entries.forEach { assetManager.load(it, type) }
    }
    assetManager.finishLoading()
  }

  /**
   * Gets an asset from the [AssetManager].
   *
   * @param source the source of the asset
   * @param type the type of the asset
   * @return the asset, or null if it doesn't exist
   */
  fun <T : Any> getAsset(source: String, type: KClass<T>): T? = assetManager.get(source, type.java)

  /**
   * Gets a [TextureAtlas] from the [AssetManager].
   *
   * @param source the source of the [TextureAtlas]
   * @return the [TextureAtlas], or null if it doesn't exist
   */
  fun getTextureAtlas(source: String): TextureAtlas? = getAsset(source, TextureAtlas::class)

  /**
   * Gets a [TextureAtlas.AtlasRegion] from the [AssetManager].
   *
   * @param source the source of the [TextureAtlas]
   * @param region the name of the region
   * @return the [TextureAtlas.AtlasRegion], or null if it doesn't exist
   */
  fun getTextureRegion(source: String, region: String) = getTextureAtlas(source)?.findRegion(region)

  /**
   * Gets a [Sound] from the [AssetManager].
   *
   * @param source the source of the [Sound]
   * @return the [Sound], or null if it doesn't exist
   */
  fun getSound(source: String) = getAsset(source, Sound::class)

  /**
   * Gets a [Music] from the [AssetManager].
   *
   * @param source the source of the [Music]
   * @return the [Music], or null if it doesn't exist
   */
  fun getMusic(source: String) = getAsset(source, Music::class)

  /**
   * Gets all of the [Sound]s from the [AssetManager].
   *
   * @return a [HashMap] of the [Sound]s, with the source as the key
   */
  fun getAllSounds(): ObjectMap<String, Sound> {
    val sounds = ObjectMap<String, Sound>()
    for (i in 0 until soundEntries.size) {
      val entry = soundEntries[i]
      val sound = getSound(entry)
      if (sound != null) {
        sounds.put(entry, sound)
      }
    }
    return sounds
  }

  /**
   * Gets all of the [Music]s from the [AssetManager].
   *
   * @return a [HashMap] of the [Music]s, with the source as the key
   */
  fun getAllMusic(): ObjectMap<String, Music> {
    val music = ObjectMap<String, Music>()
    for (i in 0 until musicEntries.size) {
        val entry = musicEntries[i]
        val m = getMusic(entry)
        if (m != null) {
            music.put(entry, m)
        }
    }
    return music
  }

  override fun dispose() = assetManager.dispose()
}
