package com.engine.factories

import com.badlogic.gdx.utils.ObjectMap

/**
 * A collection of factories that fetch objects from a key.
 *
 * @param T the type of object to fetch
 */
class Factories<T> {

  val factories = ObjectMap<String, Factory<T>>()

  /**
   * Add a factory to the collection.
   *
   * @param factoryKey the key of the factory
   * @param factory the factory to add
   */
  fun add(factoryKey: String, factory: Factory<T>): Factory<T> = factories.put(factoryKey, factory)

  /**
   * Remove a factory from the collection.
   *
   * @param factoryKey the key of the factory to remove
   */
  fun remove(factoryKey: String): Factory<T> = factories.remove(factoryKey)

  /**
   * Fetch an object from the given factory and key.
   *
   * @param factoryKey the key of the factory to fetch the object from
   */
  fun fetch(factoryKey: String, objKey: String) = factories[factoryKey]?.fetch(objKey)
}
