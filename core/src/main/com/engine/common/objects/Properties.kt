package com.engine.common.objects

import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Creates a [Properties] instance with the given key-value pairs.
 *
 * @param pairs The key-value pairs.
 * @return A [Properties] instance with the given key-value pairs.
 */
fun props(vararg pairs: Pair<String, Any?>) =
    Properties().apply { pairs.forEach { put(it.first, it.second) } }

/** A [HashMap] that stores [String] keys and [Any] type values. */
class Properties : HashMap<String, Any?> {

  /**
   * Constructs an empty [Properties] instance with the specified initial capacity and load factor.
   *
   * @param initialCapacity the initial capacity
   * @param loadFactor the load factor
   */
  constructor(initialCapacity: Int, loadFactor: Float) : super(initialCapacity, loadFactor)

  /**
   * Constructs an empty [Properties] instance with the specified initial capacity and the default
   * load factor (0.75).
   *
   * @param initialCapacity the initial capacity.
   * @throws IllegalArgumentException if the initial capacity is negative.
   */
  constructor(initialCapacity: Int) : super(initialCapacity)

  /**
   * Constructs an empty [Properties] instance with the default initial capacity (16) and the
   * default load factor (0.75).
   */
  constructor() : super()

  /**
   * Constructs a new [Properties] instance with the same mappings as the specified map.
   *
   * @param m the map whose mappings are to be placed in this map.
   */
  constructor(m: MutableMap<String, Any?>) : super(m)

  /**
   * Gets a property and casts it.
   *
   * @param key The key of the property.
   * @param type The type to cast the property to.
   * @return The property cast to the given type.
   */
  fun <T : Any> get(key: String, type: KClass<T>) = get(key)?.let { type.cast(it) }
}
