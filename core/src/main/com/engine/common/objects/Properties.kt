package com.engine.common.objects

import kotlin.reflect.KClass
import kotlin.reflect.cast

/** A [HashMap] that stores [String] keys and [Any] type values. */
class Properties : HashMap<String, Any?>() {

  /**
   * Gets a property and casts it.
   *
   * @param key The key of the property.
   * @param type The type to cast the property to.
   * @return The property cast to the given type.
   */
  fun <T : Any> get(key: String, type: KClass<T>) = get(key)?.let { type.cast(it) }
}
