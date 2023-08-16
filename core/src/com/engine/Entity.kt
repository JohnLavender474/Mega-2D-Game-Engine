package com.engine

import kotlin.reflect.KClass

abstract class Entity {
    protected val components: HashMap<KClass<out Component>, Component> = HashMap()
    protected val properties: HashMap<String, Any?> = HashMap()

    var dead = true

    abstract fun init(data: HashMap<String, Any>)

    fun putProperty(key: String, p: Any?) = properties.put(key, p)

    fun getProperty(key: String) = properties[key]

    fun hasProperty(key: String) = properties.containsKey(key)

    fun removeProperty(key: String) = properties.remove(key)

    fun putComponent(c: Component) = components.put(c::class, c)

    fun getComponent(c: KClass<out Component>) = components[c]

    fun hasComponent(c: KClass<out Component>) = components.containsKey(c)

    fun removeComponent(c: KClass<out Component>) = components.remove(c)

    override fun toString() =
            "${this::class.simpleName}: ${components.keys.mapNotNull { it.simpleName }.joinToString { it }}"
}