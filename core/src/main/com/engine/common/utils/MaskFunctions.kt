package com.engine.common.utils

import kotlin.reflect.KClass

fun <T> maskTypes(s1: T, s2: T, c: KClass<*>) = c.isInstance(s1) && c.isInstance(s2)

inline fun <reified A, reified B> maskTypes(s1: Any, s2: Any): Pair<A, B>? {
  var p: Pair<A, B>? = null
  if (A::class.isInstance(s1) && B::class.isInstance(s2)) {
    p = Pair(s1 as A, s2 as B)
  } else if (A::class.isInstance(s2) && B::class.isInstance(s1)) {
    p = Pair(s2 as A, s1 as B)
  }
  return p
}
