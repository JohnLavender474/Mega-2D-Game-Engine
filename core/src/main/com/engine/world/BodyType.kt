package com.engine.world

/**
 * An enum class that represents the type of [Body]. A [Body] can be either [ABSTRACT], [STATIC], or
 * [DYNAMIC]. An [ABSTRACT] [Body] is a [Body] that is not meant to collide with other [Body]s. A
 * [STATIC] [Body] is a [Body] that is meant to collide with other [Body]s, but is not meant to
 * move. A [DYNAMIC] [Body] is a [Body] that is meant to collide with other [Body]s and is meant to
 * move. A [Body] can only be one of these types.
 */
enum class BodyType {
  ABSTRACT,
  STATIC,
  DYNAMIC
}
