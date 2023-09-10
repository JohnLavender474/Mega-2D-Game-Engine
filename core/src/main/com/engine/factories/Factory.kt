package com.engine.factories

/**
 * A factory that fetches objects from a key.
 *
 * @param T the type of object to fetch
 */
interface Factory<T> {

  /**
   * Fetches an object from the given key.
   *
   * @param key the key to fetch the object from
   * @return the object fetched from the key
   */
  fun fetch(key: String): T
}
