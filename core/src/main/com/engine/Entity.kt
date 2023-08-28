package com.engine

import com.engine.common.interfaces.Resettable
import kotlin.reflect.KClass
import kotlin.reflect.cast

abstract class Entity : Resettable {
  internal val components: HashMap<KClass<out Component>, Component> = HashMap()
  internal val properties: HashMap<String, Any?> = HashMap()

  var dead = true

  abstract fun init(data: HashMap<String, Any?>)

  abstract fun runOnDeath()

  fun putProperty(key: String, p: Any?) = properties.put(key, p)

  fun putAllProperties(p: HashMap<String, Any?>) = properties.putAll(p)

  fun getProperty(key: String) = properties[key]

  fun hasProperty(key: String) = properties.containsKey(key)

  fun removeProperty(key: String) = properties.remove(key)

  fun clearProperties() = properties.clear()

  fun putComponent(c: Component) = components.put(c::class, c)

  fun putAllComponents(p: Map<KClass<out Component>, Component>) = components.putAll(p)

  fun <C : Component> getComponent(c: KClass<C>) =
      if (hasComponent(c)) c.cast(components[c]) else null

  fun hasComponent(c: KClass<out Component>) = components.containsKey(c)

  fun removeComponent(c: KClass<out Component>) = components.remove(c)

  fun clearComponents() = components.clear()

  override fun toString() =
      "${this::class.simpleName}: ${components.keys.mapNotNull { it.simpleName }.joinToString { it }}"
}
