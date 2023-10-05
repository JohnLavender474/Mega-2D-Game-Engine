package com.engine.entities

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass
import kotlin.reflect.cast

/** Abstract implementation for [IGameEntity]. */
abstract class GameEntity : IGameEntity {

  val componentMap = ObjectMap<KClass<out IGameComponent>, IGameComponent>()
  override val properties = Properties()
  override var dead = true

  override fun addComponent(c: IGameComponent) {
    componentMap.put(c::class, c)
  }

  override fun <C : IGameComponent> getComponent(c: KClass<C>) =
      if (hasComponent(c)) c.cast(componentMap[c]) else null

  override fun getComponents(): Iterable<IGameComponent> = componentMap.values()

  override fun hasComponent(c: KClass<out IGameComponent>) = componentMap.containsKey(c)

  override fun removeComponent(c: KClass<out IGameComponent>): IGameComponent =
      componentMap.remove(c)

  override fun clearComponents() = componentMap.clear()

  override fun toString() =
      "${this::class.simpleName}: ${componentMap.keys().mapNotNull { it.simpleName }.joinToString { it }}"
}
