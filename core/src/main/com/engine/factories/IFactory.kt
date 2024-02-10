package com.engine.factories

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
     * @return the object fetched from the key
     */
    fun fetch(key: Any): T?
}
