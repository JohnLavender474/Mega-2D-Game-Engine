package com.engine

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*

class GameAssetManagerTest :
    DescribeSpec({
      describe("GameAssetManager") {
        val musicEntries = listOf("music1.mp3", "music2.mp3")
        val soundEntries = listOf("sound1.wav", "sound2.wav")
        val textureAtlasEntries = listOf("atlas1.atlas", "atlas2.atlas")

        lateinit var mockAssetManager: AssetManager
        lateinit var gameAssetManager: GameAssetManager

        beforeTest {
          clearAllMocks()

          mockAssetManager = mockk {
            every { load<Any>(any(), any()) } just Runs
            musicEntries.forEach { every { get(it, Music::class.java) } returns mockk() }
            soundEntries.forEach { every { get(it, Sound::class.java) } returns mockk() }
            textureAtlasEntries.forEach {
              every { get(it, TextureAtlas::class.java) } returns mockk()
            }
            every { finishLoading() } just Runs
            every { dispose() } just Runs
          }

          gameAssetManager =
              spyk(
                  GameAssetManager(
                      musicEntries,
                      soundEntries,
                      textureAtlasEntries,
                      assetManager = mockAssetManager)) {
                    every { getMusic(any()) } answers
                        {
                          val source = arg<String>(0)
                          if (musicEntries.contains(source)) mockk() else null
                        }
                    every { getSound(any()) } answers
                        {
                          val source = arg<String>(0)
                          if (soundEntries.contains(source)) mockk() else null
                        }
                    every { getTextureAtlas(any()) } answers
                        {
                          val source = arg<String>(0)
                          if (textureAtlasEntries.contains(source))
                              mockk { every { findRegion(any()) } returns mockk() }
                          else null
                        }
                  }
        }

        it("should load correctly") {
          gameAssetManager.loadAssets()

          musicEntries.forEach {
            verify(exactly = 1) { mockAssetManager.load(it, Music::class.java) }
          }
          soundEntries.forEach {
            verify(exactly = 1) { mockAssetManager.load(it, Sound::class.java) }
          }
          textureAtlasEntries.forEach {
            verify(exactly = 1) { mockAssetManager.load(it, TextureAtlas::class.java) }
          }

          verify(exactly = 1) { mockAssetManager.finishLoading() }
        }

        it("should return mock for music asset") {
          val musicAsset = gameAssetManager.getMusic("music1.mp3")
          musicAsset shouldNotBe null
        }

        it("should return null for missing music asset") {
          val musicAsset = gameAssetManager.getMusic("nonexistent.mp3")
          musicAsset shouldBe null
        }

        it("should return mock for sound asset") {
          val soundAsset = gameAssetManager.getSound("sound1.wav")
          soundAsset shouldNotBe null
        }

        it("should return null for missing sound asset") {
          val soundAsset = gameAssetManager.getSound("nonexistent.wav")
          soundAsset shouldBe null
        }

        it("should return mock for texture atlas asset") {
          val atlasAsset = gameAssetManager.getTextureAtlas("atlas1.atlas")
          atlasAsset shouldNotBe null
        }

        it("should return null for missing texture atlas asset") {
          val atlasAsset = gameAssetManager.getTextureAtlas("nonexistent.atlas")
          atlasAsset shouldBe null
        }

        it("should return mock for texture region") {
          val region = gameAssetManager.getTextureRegion("atlas1.atlas", "region")
          region shouldNotBe null
        }

        it("should return null for missing texture region") {
          val region = gameAssetManager.getTextureRegion("nonexistent.atlas", "region")
          region shouldBe null
        }

        it("should get all sounds") {
          val sounds = gameAssetManager.getAllSounds()
          sounds.size shouldBe soundEntries.size
          sounds.keys.forEach {
            soundEntries.contains(it) shouldBe true
            sounds[it] shouldNotBe null
          }
        }

        it("should get all music") {
          val music = gameAssetManager.getAllMusic()
          music.size shouldBe musicEntries.size
          music.keys.forEach {
            musicEntries.contains(it) shouldBe true
            music[it] shouldNotBe null
          }
        }

        it("should dispose correctly") {
          gameAssetManager.dispose()
          verify(exactly = 1) { mockAssetManager.dispose() }
        }
      }
    })
