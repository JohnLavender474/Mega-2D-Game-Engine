package com.engine.entities

import com.engine.common.interfaces.Propertizable
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass

/**
 * An [IGameEntity] is a container for [IGameComponent]s. It is the base class for all entities in
 * the game. It is also a [Propertizable] object, so it can have properties added to it.
 *
 * @see Propertizable
 * @see IGameComponent
 */
interface IGameEntity : Propertizable {

  /**
   * True if this [GameEntity] is dead, otherwise false. A dead [GameEntity] should be removed from
   * the game.
   */
  var dead: Boolean

  /**
   * Initializes this [GameEntity] with the given [HashMap] of data.
   *
   * @param spawnProps The [HashMap] of data to initialize this [GameEntity] with.
   */
  fun spawn(spawnProps: Properties)

  /** Logic to run when this entity dies. */
  fun destroy()

  /**
   * Adds a [IGameComponent] to this [IGameEntity].
   *
   * @param c The [IGameComponent] to add.
   */
  fun addComponent(c: IGameComponent)

  /**
   * Gets a [IGameComponent] from this [IGameEntity].
   *
   * @param c The [KClass] of the [IGameComponent] to get.
   * @return The [IGameComponent] if it exists, otherwise null.
   */
  fun <C : IGameComponent> getComponent(c: KClass<C>): C?

  /**
   * Gets all the [IGameComponent]s from this [IGameEntity].
   *
   * @return All the [IGameComponent]s from this [IGameEntity].
   */
  fun getComponents(): Iterable<IGameComponent>

  /**
   * Checks if this [GameEntity] has a [IGameComponent] with the given [KClass].
   *
   * @param c The [KClass] of the [IGameComponent] to check for.
   * @return True if this [GameEntity] has a [IGameComponent] with the given [KClass], otherwise
   *   false.
   */
  fun hasComponent(c: KClass<out IGameComponent>): Boolean

  /**
   * Removes a [IGameComponent] from this [IGameEntity].
   *
   * @param c The [KClass] of the [IGameComponent] to remove.
   * @return The [IGameComponent] that was removed.
   */
  fun removeComponent(c: KClass<out IGameComponent>): IGameComponent

  /** Clears all the [IGameComponent]s from this [IGameEntity]. */
  fun clearComponents()
}
