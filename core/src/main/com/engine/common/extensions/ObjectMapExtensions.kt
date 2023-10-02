package com.engine.common.extensions

import com.badlogic.gdx.utils.ObjectMap

fun <T, U> objectMapOf(vararg pairs: Pair<T, U>): ObjectMap<T, U> {
  val map = ObjectMap<T, U>()
  pairs.forEach { map.put(it.first, it.second) }
  return map
}
