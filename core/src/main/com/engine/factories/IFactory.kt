package com.engine.factories

import com.engine.common.objects.Properties

/**
 * A factory that fetches objects from a key.
 *
 * @param T the type of object to fetch
 */
interface IFactory<T> {

  /**
   * Fetches an object using the given key.
   *
   * @param key the key of the object to fetch
   * @param props the properties to pass to the factory
   * @return the object fetched from the key
   */
  fun fetch(key: Any, props: Properties): T?
}
