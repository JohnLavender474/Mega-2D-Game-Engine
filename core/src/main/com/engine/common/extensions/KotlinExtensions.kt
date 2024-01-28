package com.engine.common.extensions

/**
 * Converts this Kotlin array to a libGDX array.
 *
 * @return the libGDX array created from this Kotlin array
 */
fun <T> Array<T>.toGdxArray(): com.badlogic.gdx.utils.Array<T> {
  val gdxArray = com.badlogic.gdx.utils.Array<T>()
  forEach { gdxArray.add(it) }
  return gdxArray
}
