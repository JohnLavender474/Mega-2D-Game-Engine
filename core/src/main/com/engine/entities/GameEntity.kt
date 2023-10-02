package com.engine.entities

import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.interfaces.Propertizable
import com.engine.common.interfaces.Resettable
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * An abstract class for objects that can be used in the game.
 *
 * @see Resettable
 * @see Propertizable
 */
abstract class GameEntity() : Resettable, Propertizable {

  val components = ObjectMap<KClass<out IGameComponent>, IGameComponent>()
  override val properties = Properties()
  var dead = true

  /**
   * Creates a [GameEntity] with the given [IGameComponent]s.
   *
   * @param components The [IGameComponent]s to add to this [GameEntity].
   */
  constructor(vararg components: IGameComponent) : this() {
    components.forEach { addComponent(it) }
  }

  /**
   * Creates a [GameEntity] with the given [Collection] of [IGameComponent]s.
   *
   * @param components The [Collection] of [IGameComponent]s to add to this [GameEntity].
   */
  constructor(components: Collection<IGameComponent>) : this() {
    components.forEach { addComponent(it) }
  }

  /**
   * Initializes this [GameEntity] with the given [HashMap] of data.
   *
   * @param spawnProps The [HashMap] of data to initialize this [GameEntity] with.
   */
  abstract fun spawn(spawnProps: Properties)

  /** Logic to run when this entity dies. */
  abstract fun destroy()

  /**
   * Adds a [IGameComponent] to this [GameEntity].
   *
   * @param c The [IGameComponent] to add.
   */
  fun addComponent(c: IGameComponent) {
    components.put(c::class, c)
  }

  /**
   * Gets a [IGameComponent] from this [GameEntity].
   *
   * @param c The [KClass] of the [IGameComponent] to get.
   * @return The [IGameComponent] if it exists, otherwise null.
   */
  fun <C : IGameComponent> getComponent(c: KClass<C>) =
      if (hasComponent(c)) c.cast(components[c]) else null

  /**
   * Checks if this [GameEntity] has a [IGameComponent] with the given [KClass].
   *
   * @param c The [KClass] of the [IGameComponent] to check for.
   * @return True if this [GameEntity] has a [IGameComponent] with the given [KClass], otherwise
   *   false.
   */
  fun hasComponent(c: KClass<out IGameComponent>) = components.containsKey(c)

  /**
   * Removes a [IGameComponent] from this [GameEntity].
   *
   * @param c The [KClass] of the [IGameComponent] to remove.
   * @return The [IGameComponent] that was removed.
   */
  fun removeComponent(c: KClass<out IGameComponent>): IGameComponent = components.remove(c)

  /** Clears all the [IGameComponent]s from this [GameEntity]. */
  fun clearComponents() = components.clear()

  override fun toString() =
      "${this::class.simpleName}: ${components.keys().mapNotNull { it.simpleName }.joinToString { it }}"
}
