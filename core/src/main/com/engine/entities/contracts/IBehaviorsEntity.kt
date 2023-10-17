package com.engine.entities.contracts

import com.engine.behaviors.BehaviorsComponent
import com.engine.behaviors.IBehavior
import com.engine.entities.IGameEntity

/**
 * An interface for [IGameEntity]s that have [IBehavior]s.
 *
 * @see [com.engine.behaviors.BehaviorsComponent]
 */
interface IBehaviorsEntity : IGameEntity {

  /**
   * Returns the [BehaviorsComponent] of this [IBehaviorsEntity].
   *
   * @return The [BehaviorsComponent] of this [IBehaviorsEntity].
   */
  fun getBehaviorsComponent() = getComponent(BehaviorsComponent::class)

  /**
   * Returns if the [IBehavior] with the given [key] is active.
   *
   * @param key The key of the [IBehavior] to check.
   * @return If the [IBehavior] with the given [key] is active.
   */
  fun isBehaviorActive(key: Any) = getBehaviorsComponent()?.isBehaviorActive(key) ?: false

  /** @see [isBehaviorActive(Iterable<Any>)] */
  fun isAnyBehaviorActive(vararg keys: Any) = isAnyBehaviorActive(keys.asIterable())

  /**
   * Returns if any of the [IBehavior]s with the given [keys] are active.
   *
   * @param keys The keys of the [IBehavior]s to check.
   * @return If any of the [IBehavior]s with the given [keys] are active.
   */
  fun isAnyBehaviorActive(keys: Iterable<Any>) = keys.any { isBehaviorActive(it) }

  /** @see [areAllBehaviorsActive(Iterable<Any>)] */
  fun areAllBehaviorsActive(vararg keys: Any) = areAllBehaviorsActive(keys.asIterable())

  /**
   * Returns if all of the [IBehavior]s with the given [keys] are active.
   *
   * @param keys The keys of the [IBehavior]s to check.
   * @return If all of the [IBehavior]s with the given [keys] are active.
   */
  fun areAllBehaviorsActive(keys: Iterable<Any>) = keys.all { isBehaviorActive(it) }
}
