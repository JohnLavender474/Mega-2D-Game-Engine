package com.engine

import kotlin.reflect.KClass
import kotlin.reflect.cast

abstract class Entity {
  val components: HashMap<KClass<out Component>, Component> = HashMap()
  val properties: HashMap<String, Any?> = HashMap()

  var dead = true

  abstract fun init(data: HashMap<String, Any>)

  fun putProperty(key: String, p: Any?) = properties.put(key, p)

  fun getProperty(key: String) = properties[key]

  fun hasProperty(key: String) = properties.containsKey(key)

  fun removeProperty(key: String) = properties.remove(key)

  fun putComponent(c: Component) = components.put(c::class, c)

  fun <C : Component> getComponent(c: KClass<C>) = c.cast(components[c])

  fun hasComponent(c: KClass<out Component>) = components.containsKey(c)

  fun removeComponent(c: KClass<out Component>) = components.remove(c)

  override fun toString() =
      "${this::class.simpleName}: ${components.keys.mapNotNull { it.simpleName }.joinToString { it }}"
}
