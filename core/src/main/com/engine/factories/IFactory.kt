package com.engine.factories

/**
 * A factory that fetches objects from a key.
 *
 * @param T the type of object to fetch
 */
interface IFactory<T> {

  /**
   * Fetches an object from the given key.
   *
   * @param key the key to fetch the object from
   * @return the object fetched from the key
   */
  fun fetch(key: Any): T?
}
