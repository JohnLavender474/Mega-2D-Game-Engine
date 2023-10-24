package com.engine

import com.badlogic.gdx.utils.ObjectMap
import com.engine.tests.KeyboardTest
import com.engine.utils.GdxTest

object GdxTests {

  const val KEYBOARD_TEST = "keyboard_test"

  private val tests = ObjectMap<String, GdxTest>()

  init {
    tests.put(KEYBOARD_TEST, KeyboardTest())
  }

  fun get(name: String): GdxTest? = tests[name]
}
