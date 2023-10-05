package com.engine.systems

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.engine.IGameEngine
import com.engine.common.interfaces.Resettable
import com.engine.common.objects.ImmutableCollection
import com.engine.common.objects.MutableOrderedSet
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity
import kotlin.reflect.KClass

/**
 * An abstract implementation of [IGameSystem]. It contains a [componentMask] which determines which
 * [IGameEntity]s it processes. It also contains a [Collection] of [IGameEntity]s that it processes.
 * The [Collection] by default is a [LinkedHashSet], which ensures that each entity is not processed
 * more than once per update and at the same time retains insertion order; but the [Collection] type
 * can be changed by overriding the [entities] property. It is recommended that the [entities]
 * property NOT be modified outside the [GameSystem] class. Instead, the user should use the [add]
 * and [remove] methods.
 *
 * @param componentMask the [KClass]es of [IGameComponent]s that this [GameSystem] accepts
 * @param entities the [Collection] of [IGameEntity]s that this [GameSystem] processes
 */
abstract class GameSystem(
    componentMask: Iterable<KClass<out IGameComponent>>,
    private val entities: MutableCollection<IGameEntity> = MutableOrderedSet()
) : IGameSystem {

  private val entitiesToAdd = Array<IGameEntity>()
  private val entitiesToRemove = Array<IGameEntity>()
  private val componentMask =
      ObjectSet<KClass<out IGameComponent>>().apply { componentMask.forEach { add(it) } }

  override var on = true

  var updating = true
    private set

  /** @see GameSystem(componentMask: Collection<KClass<out IGameComponent>>) */
  constructor(
      vararg componentMask: KClass<out IGameComponent>,
      entities: MutableCollection<IGameEntity> = MutableOrderedSet()
  ) : this(componentMask.toList(), entities)

  /**
   * Processes the given [IGameEntity]s. This method is called by the [update] method.
   * Implementations of this method should process the given [IGameEntity]s. The given
   * [IGameEntity]s are guaranteed to have all of the [IGameComponent]s in this [GameSystem]'s
   * [componentMask]. The collection is immutable. To make changes to the underlying collection of
   * entities, use the [add] and [remove] methods. This is to prevent
   * [ConcurrentModificationException]s and to ensure that the [IGameEntity]s are processed
   * correctly.
   *
   * @param on whether this [GameSystem] is on
   * @param entities the [Collection] of [IGameEntity]s to process
   * @param delta the time in seconds since the last frame
   */
  internal abstract fun process(
      on: Boolean,
      entities: ImmutableCollection<IGameEntity>,
      delta: Float
  )

  final override fun contains(e: IGameEntity) = entities.contains(e)

  final override fun remove(e: IGameEntity): Boolean {
    if (updating) entitiesToRemove.add(e) else entities.remove(e)
    return true
  }

  final override fun add(e: IGameEntity): Boolean =
      if (qualifies(e)) {
        entitiesToAdd.add(e)
        true
      } else false

  final override fun addAll(vararg entities: IGameEntity) = addAll(entities.toList())

  final override fun addAll(entities: Iterable<IGameEntity>) = entities.filter { !add(it) }

  final override fun qualifies(e: IGameEntity) = componentMask.all { e.hasComponent(it) }

  /**
   * Updates this [GameSystem]. This method is called by the [IGameEngine] every frame. Entities
   * that are dead or that do not qualify are removed from this [GameSystem] before the [process]
   * method is called.
   *
   * @param delta the time in seconds since the last frame
   * @see IGameEngine
   */
  override fun update(delta: Float) {
    updating = true

    entities.addAll(entitiesToAdd)
    entitiesToAdd.clear()

    entities.removeAll(entitiesToRemove)
    entitiesToRemove.clear()
    entities.removeIf { it.dead || !qualifies(it) }

    process(on, ImmutableCollection(entities), delta)

    updating = false
  }

  /**
   * Clears all [IGameEntity]s from this [GameSystem] and resets it to its default state. This
   * method is called by the [IGameEngine] when the game is reset.
   *
   * @see IGameEngine
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
