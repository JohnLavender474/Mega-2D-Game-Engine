package com.engine.common.extensions

import com.badlogic.gdx.utils.ObjectSet

fun <T> objectSetOf(vararg elements: T): ObjectSet<T> {
  val set = ObjectSet<T>()
  elements.forEach { set.add(it) }
  return set
}

fun <T> ObjectSet<T>.containsAny(vararg elements: Any?) = containsAny(elements.asIterable())

fun <T> ObjectSet<T>.containsAny(elements: Iterable<Any?>): Boolean {
  elements.forEach { if (contains(it)) return true }
  return false
}
