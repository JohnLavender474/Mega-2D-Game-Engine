package com.engine

import com.engine.common.interfaces.Propertizable
import com.engine.common.interfaces.Resettable
import com.engine.common.objects.Properties
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * An abstract class for objects that can be used in the game.
 *
 * @see Resettable
 * @see Propertizable
 */
abstract class GameEntity() : Resettable, Propertizable {

  val components = HashMap<KClass<out GameComponent>, GameComponent>()
  override val properties = Properties()
  var dead = true

  /**
   * Creates a [GameEntity] with the given [GameComponent]s.
   *
   * @param components The [GameComponent]s to add to this [GameEntity].
   */
  constructor(vararg components: GameComponent) : this() {
    components.forEach { addComponent(it) }
  }

  /**
   * Creates a [GameEntity] with the given [Collection] of [GameComponent]s.
   *
   * @param components The [Collection] of [GameComponent]s to add to this [GameEntity].
   */
  constructor(components: Collection<GameComponent>) : this() {
    components.forEach { addComponent(it) }
  }

  /**
   * Initializes this [GameEntity] with the given [HashMap] of data.
   *
   * @param spawnProps The [HashMap] of data to initialize this [GameEntity] with.
   */
  abstract fun spawn(spawnProps: Properties)

  /** Logic to run when this entity dies. */
  abstract fun runOnDeath()

  /**
   * Adds a [GameComponent] to this [GameEntity].
   *
   * @param c The [GameComponent] to add.
   * @return The [GameComponent] that was added.
   */
  fun addComponent(c: GameComponent) = components.put(c::class, c)

  /**
   * Gets a [GameComponent] from this [GameEntity].
   *
   * @param c The [KClass] of the [GameComponent] to get.
   * @return The [GameComponent] if it exists, otherwise null.
   */
  fun <C : GameComponent> getComponent(c: KClass<C>) =
      if (hasComponent(c)) c.cast(components[c]) else null

  /**
   * Checks if this [GameEntity] has a [GameComponent] with the given [KClass].
   *
   * @param c The [KClass] of the [GameComponent] to check for.
   * @return True if this [GameEntity] has a [GameComponent] with the given [KClass], otherwise
   *   false.
   */
  fun hasComponent(c: KClass<out GameComponent>) = components.containsKey(c)

  /**
   * Removes a [GameComponent] from this [GameEntity].
   *
   * @param c The [KClass] of the [GameComponent] to remove.
   * @return The [GameComponent] that was removed.
   */
  fun removeComponent(c: KClass<out GameComponent>) = components.remove(c)

  /** Clears all the [GameComponent]s from this [GameEntity]. */
  fun clearComponents() = components.clear()

  override fun toString() =
      "${this::class.simpleName}: ${components.keys.mapNotNull { it.simpleName }.joinToString { it }}"
}
