package com.megaman.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter
import com.badlogic.gdx.utils.GdxRuntimeException
import java.util.*

// On macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {

  private const val FPS = 60
  private const val WIDTH = 1920
  private const val HEIGHT = 1080
  private const val VSYNC = false
  private const val TITLE = "GDX TEST"

  @JvmStatic
  fun main(arg: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle(TITLE)
    config.useVsync(VSYNC)
    config.setIdleFPS(FPS)
    config.setForegroundFPS(FPS)
    config.setWindowedMode(WIDTH, HEIGHT)

    val scanner = Scanner(System.`in`)
    print("Enter the name of the test to run: ")
    val testName = scanner.next()
    scanner.close()

    val test = GdxTests.get(testName) ?: throw GdxRuntimeException("Test cannot be null")

    config.setWindowListener(
        object : Lwjgl3WindowAdapter() {
          override fun iconified(isIconified: Boolean) {
            test.pause()
          }

          override fun focusGained() {
            test.resume()
          }

          override fun focusLost() {
            test.pause()
          }
        })

    Gdx.input.inputProcessor = test
    Lwjgl3Application(test, config)
  }
}
