package com.engine.common.interfaces

import com.engine.common.objects.Properties

/**
 * An interface for objects that can have properties.
 *
 * @see Properties
 */
interface Propertizable {

  /** The [Properties] of this object. */
  val properties: Properties

  /**
   * Puts a property into this object's [Properties].
   *
   * @param key The key of the property.
   * @param p The property.
   */
  fun putProperty(key: String, p: Any?) = properties.put(key, p)

  /**
   * Puts all the properties from the given [HashMap] into this object's [Properties].
   *
   * @param p The [HashMap] of properties.
   */
  fun putAllProperties(p: HashMap<String, Any?>) = properties.putAll(p)

  /**
   * Gets a property from this object's [Properties].
   *
   * @param key The key of the property.
   */
  fun getProperty(key: String) = properties[key]

  /**
   * Checks if this object's [Properties] contains a property with the given key.
   *
   * @param key The key of the property.
   */
  fun hasProperty(key: String) = properties.containsKey(key)

  /**
   * Removes a property from this object's [Properties].
   *
   * @param key The key of the property.
   */
  fun removeProperty(key: String) = properties.remove(key)

  /** Clears all the properties from this object's [Properties]. */
  fun clearProperties() = properties.clear()
}
