package com.engine.systems

import com.engine.components.IGameComponent
import com.engine.entities.GameEntity
import java.util.*
import kotlin.reflect.KClass

/**
 * An [SortingGameSystem] is a [GameSystem] that processes [GameEntity]s in a sorted order. It
 * overrides the default [Collection] type of [entities] with a [TreeSet] (instead of
 * [LinkedHashSet]) to ensure that the [GameEntity]s are processed in a sorted order.
 *
 * @param comparator the [Comparator] to sort the [GameEntity]s with
 * @param componentMask the [Collection] of [KClass]es of [IGameComponent]s that this
 */
abstract class SortingGameSystem(
    comparator: Comparator<GameEntity>,
    componentMask: Iterable<KClass<out IGameComponent>>
) : GameSystem(componentMask, TreeSet<GameEntity>(comparator)) {

  /**
   * @see SortingGameSystem(comparator: Comparator<GameEntity>, componentMask: Collection<KClass<out
   */
  constructor(
      comparator: Comparator<GameEntity>,
      vararg componentMask: KClass<out IGameComponent>
  ) : this(comparator, componentMask.toList())
}
