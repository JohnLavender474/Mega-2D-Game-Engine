package com.mega.game.engine.common.objects

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Predicate
import com.mega.game.engine.common.extensions.putIfAbsentAndGet
import com.mega.game.engine.common.interfaces.ICopyable
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Creates a [Properties] instance with the given key-value pairs.
 *
 * @param pairs The key-value pairs.
 * @return A [Properties] instance with the given key-value pairs.
 */
fun props(vararg pairs: GamePair<Any, Any?>) = Properties().apply { pairs.forEach { put(it.first, it.second) } }

/** A [HashMap] that stores [String] keys and [Any] type values. */
class Properties : ICopyable<Properties> {

    /** Returns the size of the properties, i.e. the number of entries. */
    val size: Int
        get() = map.size

    private val map: ObjectMap<Any, Any?>

    /**
     * Constructs an empty [Properties] instance with the default initial capacity (16) and the
     * default load factor (0.75).
     */
    constructor() {
        map = ObjectMap()
    }

    /**
     * Constructs an empty [Properties] instance with the specified initial capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     */
    constructor(initialCapacity: Int, loadFactor: Float) {
        map = ObjectMap(initialCapacity, loadFactor)
    }

    /**
     * Constructs an empty [Properties] instance with the specified initial capacity and the default
     * load factor (0.75).
     *
     * @param initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    constructor(initialCapacity: Int) {
        map = ObjectMap(initialCapacity)
    }

    /**
     * Constructs a new [Properties] instance with the same mappings as the specified map. The keys
     * are converted to [String]s via the objects' [Any.toString] method.
     *
     * @param m the map whose mappings are to be placed in this map.
     */
    constructor(m: ObjectMap<Any, Any?>) : this() {
        m.forEach { put(it.key, it.value) }
    }

    /**
     * Constructs a new [Properties] instance with the same mapping as the specified [props] object.
     *
     * @param props the props object to copy
     */
    constructor(props: Properties) : this(props.map)

    /**
     * Gets a property and casts it.
     *
     * @param key The key of the property.
     * @param type The type to cast the property to.
     * @return The property cast to the given type.
     */
    fun <T : Any> get(key: Any, type: KClass<T>) = map.get(key)?.let { type.cast(it) }

    /**
     * Gets a property and casts it.
     *
     * @param key The key of the property.
     * @param type The type to cast the property to.
     * @return The property cast to the given type.
     */
    fun <T : Any> get(key: Any, type: Class<T>) = map.get(key)?.let { type.cast(it) }

    /**
     * Puts a property into this [Properties] instance.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @return The previous value of the property, or null if it did not have one.
     */
    fun put(key: Any, value: Any?) = map.put(key, value)

    /**
     * Puts a property into this [Properties] instance if it does not already have a value. If the
     * property does have a value then the value is returned.
     *
     * @param key The key of the property.
     * @param defaultValue The default value of the property.
     * @return The current (existing or computed) value associated with the specified key, or null if
     *   the computed value is null.
     */
    fun putIfAbsentAndGet(key: Any, defaultValue: Any?) = map.putIfAbsentAndGet(key, defaultValue)

    /** Clears this [Properties] instance. */
    fun clear() = map.clear()

    /**
     * Returns if this [Properties] instance is empty.
     *
     * @return if this [Properties] instance is empty
     */
    fun isEmpty() = map.isEmpty

    /**
     * Removes a property from this [Properties] instance.
     *
     * @param key The key of the property.
     * @return The previous value of the property, or null if it did not have one.
     */
    fun remove(key: Any) = map.remove(key)

    /**
     * Puts all the properties from the given [ObjectMap] into this [Properties] instance.
     *
     * @param from The [ObjectMap] of properties.
     */
    fun putAll(from: ObjectMap<Any, Any?>) = map.putAll(from)

    /**
     * Puts all the properties from the given [Properties] into this [Properties] instance.
     *
     * @param _props The [Properties] of properties.
     */
    fun putAll(_props: Properties) = _props.forEach { key, value -> put(key, value) }

    /**
     * Puts the pairs into this properties object.
     *
     * @param _props the pairs
     */
    fun <K : Any, V> putAll(vararg _props: GamePair<K, V>) = _props.forEach { put(it.first, it.second) }

    /**
     * Gets a property from this [Properties] instance.
     *
     * @param key The key of the property.
     * @return The property.
     */
    fun get(key: Any) = map.get(key)

    /**
     * Checks if this [Properties] instance contains a property with the given key and value.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @return True if this [Properties] instance contains a property with the given key and value, otherwise false.
     */
    fun isProperty(key: Any, value: Any) = map.get(key) == value

    /**
     * Gets all the properties from this [Properties] instance where the key matches the given predicate.
     *
     * @param keyPredicate The predicate to match.
     * @return An array of properties where the key matches the given predicate.
     */
    fun getAllMatching(keyPredicate: (Any) -> Boolean): Array<GamePair<Any, Any?>> {
        val matching = Array<GamePair<Any, Any?>>()
        forEach { key, value -> if (keyPredicate(key)) matching.add(GamePair(key, value)) }
        return matching
    }

    /**
     * Gets all the properties from this [Properties] instance where the key matches the given predicate.
     *
     * @param keyPredicate The predicate to match.
     * @return An array of properties where the key matches the given predicate.
     */
    fun getAllMatching(keyPredicate: Predicate<Any>): Array<GamePair<Any, Any?>> {
        val matching = Array<GamePair<Any, Any?>>()
        forEach { key, value -> if (keyPredicate.evaluate(key)) matching.add(GamePair(key, value)) }
        return matching
    }

    /**
     * Gets a property from this [Properties] instance. If no property is mapped to the given [key],
     * then the [defaultValue] is returned instead.
     *
     * @param key The key of the property.
     * @param defaultValue The default value of the property.
     * @return The property, or the default value if no property is mapped to the given key.
     */
    fun getOrDefault(key: Any, defaultValue: Any?) = if (containsKey(key)) get(key) else defaultValue

    /**
     * Gets a property from this [Properties] instance. If no property is mapped to the given [key],
     * then the [defaultValue] is returned instead.
     *
     * @param key The key of the property.
     * @param defaultValue The default value of the property. Cannot be null.
     * @param type The type to cast the property to.
     * @return The property, or the default value if no property is mapped to the given key.
     */
    fun <T : Any> getOrDefault(key: Any, defaultValue: Any, type: KClass<T>) =
        type.cast(if (containsKey(key)) get(key, type) else defaultValue)

    /**
     * Gets a property from this [Properties] instance. If no property is mapped to the given [key],
     * then the [defaultValue] is returned instead.
     *
     * @param key The key of the property.
     * @param defaultValue The default value of the property. Cannot be null.
     * @param type The type to cast the property to.
     */
    fun <T : Any> getOrDefault(key: Any, defaultValue: Any, type: Class<T>): T =
        type.cast(if (containsKey(key)) get(key, type) else defaultValue)

    /**
     * Gets a property from this [Properties] instance. If no property is mapped to the given [key],
     * then the [defaultValue] is returned instead.
     *
     * @param key The key of the property.
     * @param defaultValue The default value of the property. Cannot be null.
     * @param type The type to cast the property to.
     * @return The property, or the default value if no property is mapped to the given key.
     */
    fun <T : Any> getOrDefaultNotNull(key: Any, defaultValue: Any, type: KClass<T>): T {
        val value = get(key)
        return if (value != null) type.cast(value) else type.cast(defaultValue)
    }

    /**
     * Gets a property from this [Properties] instance. If no property is mapped to the given [key],
     * then the [defaultValue] is returned instead.
     *
     * @param key The key of the property.
     * @param defaultValue The default value of the property. Cannot be null.
     * @param type The type to cast the property to.
     * @return The property, or the default value if no property is mapped to the given key.
     */
    fun <T : Any> getOrDefaultNotNull(key: Any, defaultValue: Any, type: Class<T>): T {
        val value = get(key)
        return if (value != null) type.cast(value) else type.cast(defaultValue)
    }

    /**
     * Gets a property from this [Properties] instance. If the property is null then an exception is
     * thrown.
     *
     * @param key The key of the property.
     * @return The property.
     */
    fun getNotNull(key: Any) = map.get(key)!!

    /**
     * Returns if this [Properties] instance contains the given key.
     *
     * @param key The key to check.
     * @return if this [Properties] instance contains the given key
     */
    fun containsKey(key: Any) = map.containsKey(key)

    /**
     * Performs the given action for each property in this [Properties] instance.
     *
     * @param action The action to perform.
     */
    fun forEach(action: (key: Any, value: Any?) -> Unit) {
        for (entry in map.entries()) action(entry.key, entry.value)
    }

    /**
     * Returns a new [Properties] instance containing all the properties from this [Properties]
     * instance that match the given predicate.
     *
     * @param predicate The predicate to match.
     * @return A [Properties] instance containing all the properties from this [Properties] instance
     */
    fun collect(predicate: (key: Any, value: Any?) -> Boolean): Properties {
        val collected = Properties()
        forEach { key, value -> if (predicate(key, value)) collected.put(key, value) }
        return collected
    }

    /**
     * Returns a copy of this [Properties] instance.
     *
     * @return a copy of this [Properties] instance
     */
    override fun copy() = Properties(map)

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Properties({")
        forEach { key, value -> sb.append("[$key=$value]") }
        sb.append("})")
        return sb.toString()
    }
}
