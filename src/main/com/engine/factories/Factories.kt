package com.engine.factories

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap

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
     * @return the object fetched from the key
     */
    open fun fetch(factoryKey: Any, objKey: Any) = factories[factoryKey]?.fetch(objKey)

    /**
     * Fetch a collection of objects from the given factory and key.
     *
     * @param factoryKey the key of the factory to fetch the objects from
     * @param objKey the key of the object to fetch
     * @param count the number of objects to fetch
     * @return the collection of objects fetched from the key
     */
    open fun fetch(factoryKey: Any, objKey: Any, count: Int): Array<T> {
        val factory = factories[factoryKey] ?: throw IllegalArgumentException("Factory not found for key: $factoryKey")
        val objects = Array<T>()
        repeat(count) {
            val obj = factory.fetch(objKey) ?: throw IllegalArgumentException("Object not found for key: $objKey")
            objects.add(obj)
        }
        return objects
    }
}
