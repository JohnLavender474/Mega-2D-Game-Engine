package com.engine.common.extensions

import com.badlogic.gdx.utils.ObjectSet

fun <T> objectSetOf(vararg elements: T): ObjectSet<T> {
  val set = ObjectSet<T>()
  elements.forEach { set.add(it) }
  return set
}
