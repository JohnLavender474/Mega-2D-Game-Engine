package com.mega.game.engine.components

import com.badlogic.gdx.utils.Array
import kotlin.reflect.KClass

/**
 * Interface for objects that act as component buckets.
 */
interface IComponentBucket {

    /**
     * Gets a component from this bucket using the specified key if one exists.
     *
     * @param key the key to use to fetch a component
     * @return The component if it exists, otherwise null
     */
    fun <C : IGameComponent> getComponent(key: KClass<C>): C?

    /**
     * Add the specified component with the specified key to this bucket.
     *
     * @param c The [IGameComponent] to add
     */
    fun addComponent(c: IGameComponent)

    /**
     * Adds the specified components one at a time to this bucket.
     *
     * @param components the components to add
     */
    fun addComponents(components: Array<IGameComponent>) = components.forEach { addComponent(it) }

    /**
     * Checks if this bucket has a component with the given key.
     *
     * @param key the key to use to check if the bucket has the component.
     * @return True if this bucket has a component with the given key, otherwise false
     */
    fun hasComponent(key: KClass<out IGameComponent>): Boolean

    /**
     * Removes the component with the specified key from this bucket.
     *
     * @param key the key for the component to be removed from this bucket
     */
    fun removeComponent(key: KClass<out IGameComponent>)

    /**
     * Clears the components from this bucket.
     */
    fun clearComponents()
}