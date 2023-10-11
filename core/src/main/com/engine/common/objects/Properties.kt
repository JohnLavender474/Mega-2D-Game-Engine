package com.engine.common.objects

import com.badlogic.gdx.utils.ObjectMap
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Creates a [Properties] instance with the given key-value pairs.
 *
 * @param pairs The key-value pairs.
 * @return A [Properties] instance with the given key-value pairs.
 */
fun props(vararg pairs: Pair<Any, Any?>) =
    Properties().apply { pairs.forEach { put(it.first.toString(), it.second) } }

/** A [HashMap] that stores [String] keys and [Any] type values. */
class Properties {

  private val props: ObjectMap<Any, Any?>

  /**
   * Constructs an empty [Properties] instance with the default initial capacity (16) and the
   * default load factor (0.75).
   */
  constructor() {
    props = ObjectMap()
  }

  /**
   * Constructs an empty [Properties] instance with the specified initial capacity and load factor.
   *
   * @param initialCapacity the initial capacity
   * @param loadFactor the load factor
   */
  constructor(initialCapacity: Int, loadFactor: Float) {
    props = ObjectMap(initialCapacity, loadFactor)
  }

  /**
   * Constructs an empty [Properties] instance with the specified initial capacity and the default
   * load factor (0.75).
   *
   * @param initialCapacity the initial capacity.
   * @throws IllegalArgumentException if the initial capacity is negative.
   */
  constructor(initialCapacity: Int) {
    props = ObjectMap(initialCapacity)
  }

  /**
   * Constructs a new [Properties] instance with the same mappings as the specified map. The keys
   * are converted to [String]s via the objects' [Any.toString] method.
   *
   * @param m the map whose mappings are to be placed in this map.
   */
  constructor(m: ObjectMap<Any, Any?>) : this() {
    m.forEach { put(it.key.toString(), it.value) }
  }

  /**
   * Gets a property and casts it.
   *
   * @param key The key of the property.
   * @param type The type to cast the property to.
   * @return The property cast to the given type.
   */
  fun <T : Any> get(key: Any, type: KClass<T>) = props.get(key.toString())?.let { type.cast(it) }

  /**
   * Puts a property into this [Properties] instance.
   *
   * @param key The key of the property.
   * @param value The value of the property.
   * @return The previous value of the property, or null if it did not have one.
   */
  fun put(key: Any, value: Any?) = props.put(key, value)

  /** Clears this [Properties] instance. */
  fun clear() = props.clear()

  /**
   * Returns if this [Properties] instance is empty.
   *
   * @return if this [Properties] instance is empty
   */
  fun isEmpty() = props.isEmpty

  /**
   * Removes a property from this [Properties] instance.
   *
   * @param key The key of the property.
   * @return The previous value of the property, or null if it did not have one.
   */
  fun remove(key: Any) = props.remove(key)

  /**
   * Puts all the properties from the given [ObjectMap] into this [Properties] instance.
   *
   * @param from The [ObjectMap] of properties.
   */
  fun putAll(from: ObjectMap<out Any, Any?>) = props.putAll(from)

  /**
   * Puts all the properties from the given [Properties] into this [Properties] instance.
   *
   * @param props The [Properties] of properties.
   */
  fun putAll(props: Properties) = props.putAll(props.props)

  /**
   * Gets a property from this [Properties] instance.
   *
   * @param key The key of the property.
   * @return The property.
   */
  fun get(key: Any) = props.get(key)

  /**
   * Returns if this [Properties] instance contains the given key.
   *
   * @param key The key to check.
   * @return if this [Properties] instance contains the given key
   */
  fun containsKey(key: Any) = props.containsKey(key)
}
