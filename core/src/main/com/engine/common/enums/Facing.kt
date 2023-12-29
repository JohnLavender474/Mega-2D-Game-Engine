package com.engine.common.enums

/**
 * An enum for objects that can face left or right.
 *
 * @param value The scale to apply to the object when facing this direction.
 */
enum class Facing(val value: Int) {
  LEFT(-1),
  RIGHT(1)
}
