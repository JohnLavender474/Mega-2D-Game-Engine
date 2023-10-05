package com.engine.entities

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass
import kotlin.reflect.cast

/** Abstract implementation for [IGameEntity]. */
abstract class GameEntity : IGameEntity {

  val components = ObjectMap<KClass<out IGameComponent>, IGameComponent>()
  override val properties = Properties()
  override var dead = true

  override fun addComponent(c: IGameComponent) {
    components.put(c::class, c)
  }

  override fun <C : IGameComponent> getComponent(c: KClass<C>) =
      if (hasComponent(c)) c.cast(components[c]) else null

  override fun getComponents(): Iterable<IGameComponent> = components.values()

  override fun hasComponent(c: KClass<out IGameComponent>) = components.containsKey(c)

  override fun removeComponent(c: KClass<out IGameComponent>): IGameComponent = components.remove(c)

  override fun clearComponents() = components.clear()

  override fun toString() =
      "${this::class.simpleName}: ${components.keys().mapNotNull { it.simpleName }.joinToString { it }}"
}
