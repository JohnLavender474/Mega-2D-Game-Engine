package com.engine.factories

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties

/**
 * A collection of factories that fetch objects from a key.
 *
 * @param T the type of object to fetch
 */
open class Factories<T> {

  open val factories = ObjectMap<Any, IFactory<T>>()

  /**
   * Add a factory to the collection.
   *
   * @param factoryKey the key of the factory
   * @param factory the factory to add
   */
  open fun add(factoryKey: Any, factory: IFactory<T>): IFactory<T> =
      factories.put(factoryKey, factory)

  /**
   * Remove a factory from the collection.
   *
   * @param factoryKey the key of the factory to remove
   */
  open fun remove(factoryKey: Any): IFactory<T> = factories.remove(factoryKey)

  /**
   * Fetch an object from the given factory and key.
   *
   * @param factoryKey the key of the factory to fetch the object from
   * @param objKey the key of the object to fectch
   * @param props the properties to pass to the factory
   * @return the object fetched from the key
   */
  open fun fetch(factoryKey: Any, objKey: String, props: Properties) =
      factories[factoryKey]?.fetch(objKey, props)
}
