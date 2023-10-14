package com.megaman.game

import com.badlogic.gdx.utils.ObjectMap
import com.engine.utils.GdxTest

object GdxTests {

  private val tests = ObjectMap<String, GdxTest>()

  fun get(name: String): GdxTest? = tests[name]
}
