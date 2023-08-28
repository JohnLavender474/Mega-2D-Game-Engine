package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.ImmutableCollection
import kotlin.reflect.KClass

/**
 * A [GameSystem] is an [Updatable] that processes [Entity]s. It contains a [componentMask] which
 * determines which [Entity]s it processes.
 *
 * @see Entity
 * @see Updatable
 * @see Resettable
 * @see Component
 */
abstract class GameSystem(componentMask: Collection<KClass<out Component>>) :
    Updatable, Resettable {

  private val entities: LinkedHashSet<Entity> = LinkedHashSet()
  private val entitiesToAdd: ArrayList<Entity> = ArrayList()
  private val componentMask: HashSet<KClass<out Component>> = HashSet(componentMask)

  var on = true
  var updating = true
    private set

  private var purgeEntities = false

  /**
   * Processes the given [Entity]s. This method is called by the [update] method. Implementations of
   * this method should process the given [Entity]s. The given [Entity]s are guaranteed to have all
   * of the [Component]s in this [GameSystem]'s [componentMask]. The collection is immutable. To
   * make changes to the underlying collection of entities, use the [add] and [remove] methods. This
   * is to prevent [ConcurrentModificationException]s and to ensure that the [Entity]s are processed
   * correctly.
   *
   * @param on whether this [GameSystem] is on
   * @param entities the [Collection] of [Entity]s to process
   * @param delta the time in seconds since the last frame
   */
  internal abstract fun process(on: Boolean, entities: ImmutableCollection<Entity>, delta: Float)

  /** Purges all [Entity]s from this [GameSystem]. */
  fun purge() = if (updating) purgeEntities = true else entities.clear()

  /**
   * Returns whether this [GameSystem] contains the given [Entity]. This method is called by the
   * [GameEngine] when an [Entity] is added to the game.
   *
   * @param e the [Entity] to check
   * @return whether this [GameSystem] contains the given [Entity]
   */
  fun contains(e: Entity) = entities.contains(e)

  /**
   * Removes the given [Entity] from this [GameSystem]. This method is called by the [GameEngine]
   * when an [Entity] is removed from the game.
   *
   * @param e the [Entity] to remove
   */
  fun remove(e: Entity) = if (updating) entities.remove(e) else entitiesToAdd.remove(e)

  /**
   * Adds the given [Entity] to this [GameSystem] if it qualifies. An [Entity] qualifies if it has
   * all of the [Component]s in this [GameSystem]'s [componentMask].
   *
   * @param e the [Entity] to add
   * @return whether the [Entity] was added
   */
  fun add(e: Entity) = if (qualifies(e)) entitiesToAdd.add(e) else false

  /**
   * Adds all the given [Entity]s to this [GameSystem] if they qualify. An [Entity] qualifies if
   * it has all of the [Component]s in this [GameSystem]'s [componentMask].
   *
   * @param entities the [Collection] of [Entity]s to add
   * @return the [Entity]s that could not be added
   */
  fun addAll(entities: Collection<Entity>) = entities.filter { !add(it) }

  /**
   * Returns whether the given [Entity] qualifies. An [Entity] qualifies if it has all of the
   * [Component]s in this [GameSystem]'s [componentMask].
   *
   * @param e the [Entity] to check
   * @return whether the [Entity] qualifies
   */
  fun qualifies(e: Entity) = componentMask.all { e.hasComponent(it) }

  /**
   * Updates this [GameSystem]. This method is called by the [GameEngine] every frame. Entities that
   * are dead or that do not qualify are removed from this [GameSystem] before the [process] method
   * is called.
   *
   * @param delta the time in seconds since the last frame
   * @see GameEngine
   */
  final override fun update(delta: Float) {
    updating = true
    entities.addAll(entitiesToAdd)
    entitiesToAdd.clear()
    entities.filter { !it.dead && qualifies(it) }
    process(on, ImmutableCollection(entities), delta)
    updating = false
  }

  /**
   * Clears all [Entity]s from this [GameSystem] and resets it to its default state. This method is
   * called by the [GameEngine] when the game is reset.
   *
   * @see GameEngine
   * @see Resettable
   */
  override fun reset() {
    entities.clear()
    entitiesToAdd.clear()
  }

  /** Returns the simple class name and [componentMask] of this [GameSystem]. */
  override fun toString(): String =
      "${this::class.simpleName}: ${componentMask.map { it.simpleName }.joinToString { ", " }}"
}
