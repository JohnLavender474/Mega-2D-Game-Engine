package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
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

  private val entities: ArrayList<Entity> = ArrayList()
  private val entitiesToAdd: ArrayList<Entity> = ArrayList()
  private val componentMask: HashSet<KClass<out Component>> = HashSet(componentMask)

  var on = true
  var updating = true
    private set

  private var purgeEntities = false

  /**
   * Processes the given [Entity]s. This method is called by the [update] method.
   *
   * @param on whether this [GameSystem] is on
   * @param entities the [ArrayList] of [Entity]s to process
   */
  protected abstract fun process(on: Boolean, entities: ArrayList<Entity>, delta: Float)

  /** Purges all [Entity]s from this [GameSystem]. */
  fun purgeAllEntities() = if (updating) purgeEntities = true else entities.clear()

  /**
   * Adds the given [Entity] to this [GameSystem] if it qualifies. An [Entity] qualifies if it has
   * all of the [Component]s in this [GameSystem]'s [componentMask].
   *
   * @param e the [Entity] to add
   * @return whether the [Entity] was added
   */
  fun addEntityIfQualifies(e: Entity) = if (isQualified(e)) entitiesToAdd.add(e) else false

  /**
   * Returns whether the given [Entity] qualifies. An [Entity] qualifies if it has all of the
   * [Component]s in this [GameSystem]'s [componentMask].
   *
   * @param e the [Entity] to check
   * @return whether the [Entity] qualifies
   */
  fun isQualified(e: Entity) = componentMask.all { e.hasComponent(it) }

  /**
   * Updates this [GameSystem]. This method is called by the [GameEngine] every frame. Entities that
   * are dead or that do not qualify are removed from this [GameSystem] before the [process] method
   * is called.
   *
   * @param delta the time in seconds since the last frame
   * @see GameEngine
   */
  override fun update(delta: Float) {
    updating = true
    entities.addAll(entitiesToAdd)
    entitiesToAdd.clear()
    entities.filter { !it.dead && isQualified(it) }
    process(on, entities, delta)
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
