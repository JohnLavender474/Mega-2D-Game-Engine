package com.engine.factories

import com.badlogic.gdx.utils.ObjectMap

/**
 * A collection of factories that fetch objects from a key.
 *
 * @param T the type of object to fetch
 */
open class Factories<T> {

  val factories = ObjectMap<Any, IFactory<T>>()

  /**
   * Add a factory to the collection.
   *
   * @param factoryKey the key of the factory
   * @param factory the factory to add
   */
  fun add(factoryKey: Any, factory: IFactory<T>): IFactory<T> =
      factories.put(factoryKey, factory)

  /**
   * Remove a factory from the collection.
   *
   * @param factoryKey the key of the factory to remove
   */
  fun remove(factoryKey: Any): IFactory<T> = factories.remove(factoryKey)

  /**
   * Fetch an object from the given factory and key.
   *
   * @param factoryKey the key of the factory to fetch the object from
   */
  fun fetch(factoryKey: Any, objKey: String) = factories[factoryKey]?.fetch(objKey)
}
