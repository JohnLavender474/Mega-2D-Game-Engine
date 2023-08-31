package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.ImmutableCollection
import kotlin.reflect.KClass

/**
 * A [GameSystem] is an [Updatable] that processes [GameEntity]s. It contains a [componentMask] which
 * determines which [GameEntity]s it processes.
 *
 * @see GameEntity
 * @see Updatable
 * @see Resettable
 * @see GameComponent
 */
abstract class GameSystem(componentMask: Collection<KClass<out GameComponent>>) :
    Updatable, Resettable {

  private val entities: LinkedHashSet<GameEntity> = LinkedHashSet()
  private val entitiesToAdd: ArrayList<GameEntity> = ArrayList()
  private val componentMask: HashSet<KClass<out GameComponent>> = HashSet(componentMask)

  var on = true
  var updating = true
    private set

  private var purgeEntities = false

  /**
   * Processes the given [GameEntity]s. This method is called by the [update] method. Implementations of
   * this method should process the given [GameEntity]s. The given [GameEntity]s are guaranteed to have all
   * of the [GameComponent]s in this [GameSystem]'s [componentMask]. The collection is immutable. To
   * make changes to the underlying collection of entities, use the [add] and [remove] methods. This
   * is to prevent [ConcurrentModificationException]s and to ensure that the [GameEntity]s are processed
   * correctly.
   *
   * @param on whether this [GameSystem] is on
   * @param entities the [Collection] of [GameEntity]s to process
   * @param delta the time in seconds since the last frame
   */
  internal abstract fun process(on: Boolean, entities: ImmutableCollection<GameEntity>, delta: Float)

  /** Purges all [GameEntity]s from this [GameSystem]. */
  fun purge() = if (updating) purgeEntities = true else entities.clear()

  /**
   * Returns whether this [GameSystem] contains the given [GameEntity]. This method is called by the
   * [GameEngine] when an [GameEntity] is added to the game.
   *
   * @param e the [GameEntity] to check
   * @return whether this [GameSystem] contains the given [GameEntity]
   */
  fun contains(e: GameEntity) = entities.contains(e)

  /**
   * Removes the given [GameEntity] from this [GameSystem]. This method is called by the [GameEngine]
   * when an [GameEntity] is removed from the game.
   *
   * @param e the [GameEntity] to remove
   */
  fun remove(e: GameEntity) = if (updating) entities.remove(e) else entitiesToAdd.remove(e)

  /**
   * Adds the given [GameEntity] to this [GameSystem] if it qualifies. An [GameEntity] qualifies if it has
   * all of the [GameComponent]s in this [GameSystem]'s [componentMask].
   *
   * @param e the [GameEntity] to add
   * @return whether the [GameEntity] was added
   */
  fun add(e: GameEntity) = if (qualifies(e)) entitiesToAdd.add(e) else false

  /**
   * Adds all the given [GameEntity]s to this [GameSystem] if they qualify. An [GameEntity] qualifies if
   * it has all of the [GameComponent]s in this [GameSystem]'s [componentMask].
   *
   * @param entities the [Collection] of [GameEntity]s to add
   * @return the [GameEntity]s that could not be added
   */
  fun addAll(entities: Collection<GameEntity>) = entities.filter { !add(it) }

  /**
   * Returns whether the given [GameEntity] qualifies. An [GameEntity] qualifies if it has all of the
   * [GameComponent]s in this [GameSystem]'s [componentMask].
   *
   * @param e the [GameEntity] to check
   * @return whether the [GameEntity] qualifies
   */
  fun qualifies(e: GameEntity) = componentMask.all { e.hasComponent(it) }

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
   * Clears all [GameEntity]s from this [GameSystem] and resets it to its default state. This method is
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
