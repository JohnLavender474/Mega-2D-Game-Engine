package com.engine.managers

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable
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
    private val musicEntries: Collection<String>,
    private val soundEntries: Collection<String>,
    private val textureAtlasEntries: Collection<String>,
    private val otherEntries: Map<KClass<*>, Collection<String>>? = null
) : Disposable {

  private val assMan = AssetManager()

  /**
   * Loads the assets. Must be called before any assets can be used.
   *
   * @see [AssetManager.load] and [AssetManager.finishLoading]
   */
  fun loadAssets() {
    musicEntries.forEach { assMan.load(it, Music::class.java) }
    soundEntries.forEach { assMan.load(it, Sound::class.java) }
    textureAtlasEntries.forEach { assMan.load(it, TextureAtlas::class.java) }
    otherEntries?.forEach { (type, entries) -> entries.forEach { assMan.load(it, type.java) } }
    assMan.finishLoading()
  }

  fun <T : Any> getAsset(source: String, type: KClass<T>): T? = assMan.get(source, type.java)

  fun getTextureAtlas(source: String): TextureAtlas? = getAsset(source, TextureAtlas::class)

  fun getTextureRegion(source: String, region: String) = getTextureAtlas(source)?.findRegion(region)

  fun getSound(source: String) = getAsset(source, Sound::class)

  fun getMusic(source: String) = getAsset(source, Music::class)

  fun getAllSounds(): HashMap<String, Sound> {
    val sounds = HashMap<String, Sound>()
    soundEntries.forEach {
      val sound = sounds[it]
      if (sound != null) {
        sounds[it] = sound
      }
    }
    return sounds
  }

  fun getAllMusic(): HashMap<String, Music> {
    val music = HashMap<String, Music>()
    musicEntries.forEach {
      val m = music[it]
      if (m != null) {
        music[it] = m
      }
    }
    return music
  }

  override fun dispose() = assMan.dispose()
}
