package com.engine.tests

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.IntSet
import com.engine.common.extensions.gdxArrayOf
import com.engine.controller.buttons.Button
import com.engine.controller.buttons.Buttons
import com.engine.controller.polling.ControllerPoller
import com.engine.controller.polling.IControllerPoller
import com.engine.utils.GdxTest

class KeyboardTest : GdxTest() {

  companion object {
    private val KEYS = gdxArrayOf("left", "right", "down", "up", "reset")
  }

  private lateinit var buttons: Buttons
  private lateinit var set: IntSet
  private lateinit var poller: IControllerPoller
  private lateinit var screen: Screen

  private var setting = false
  private var lastIndex = -1
  private var index = 0

  override fun create() {
    buttons = Buttons()
    buttons.put("left", Button(Input.Keys.A))
    buttons.put("right", Button(Input.Keys.D))
    buttons.put("down", Button(Input.Keys.S))
    buttons.put("up", Button(Input.Keys.W))
    buttons.put("reset", Button(Input.Keys.SPACE))
    poller = ControllerPoller(buttons)
  }

  override fun render() {
    if (poller.isButtonPressed("reset")) {
      setting = true
      index = 0
      lastIndex = -1
    }

    if (index >= KEYS.size) setting = false

    if (setting) {
      if (lastIndex != index) {
        println("Set key for ${KEYS[index]}")
        lastIndex = index
      }
      return
    }

    val delta = Gdx.app.graphics.deltaTime
    screen.render(delta)
  }

  override fun keyDown(keycode: Int): Boolean {
    if (!setting) return true

    if (set.contains(keycode)) {
      println("Keycode $keycode is already assigned")
      return true
    }

    set.add(keycode)
    buttons.get(KEYS[index]).keyboardCode = keycode
    index++

    return true
  }
}
