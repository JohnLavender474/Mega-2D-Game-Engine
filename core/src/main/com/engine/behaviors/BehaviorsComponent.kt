package com.engine.behaviors

import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.OrderedMap
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/** A [IGameComponent] that manages a collection of [AbstractBehavior]s. */
class BehaviorsComponent(override val entity: IGameEntity) : IGameComponent {

  internal val behaviors = OrderedMap<Any, AbstractBehavior>()
  private val activeBehaviors = ObjectSet<Any>()

  /**
   * Creates a [BehaviorsComponent] with the given [behaviors].
   *
   * @param _behaviors The [AbstractBehavior]s to add to this [BehaviorsComponent].
   */
  constructor(entity: IGameEntity, vararg _behaviors: Pair<Any, AbstractBehavior>) : this(entity) {
    _behaviors.forEach { addBehavior(it.first, it.second) }
  }

  /**
   * Creates a [BehaviorsComponent] with the given [behaviors].
   *
   * @param _behaviors The [AbstractBehavior]s to add to this [BehaviorsComponent].
   */
  constructor(entity: IGameEntity, _behaviors: Iterable<Pair<Any, AbstractBehavior>>) : this(entity) {
    _behaviors.forEach { addBehavior(it.first, it.second) }
  }

  /**
   * Creates a [BehaviorsComponent] with the given [behaviors].
   *
   * @param _behaviors The [AbstractBehavior]s to add to this [BehaviorsComponent].
   */
  constructor(entity: IGameEntity, _behaviors: OrderedMap<Any, AbstractBehavior>) : this(entity) {
    _behaviors.forEach { addBehavior(it.key, it.value) }
  }

  /**
   * Adds a [AbstractBehavior] to this [BehaviorsComponent] with the given [key]. If a [AbstractBehavior] already
   * exists with the given [key], it will be overwritten. Insertion order of behaviors is preserved
   * via a [LinkedHashMap]. The [AbstractBehavior] will be inactive by default.
   *
   * @param key The key to associate with the [AbstractBehavior].
   * @param behavior The [AbstractBehavior] to add.
   */
  fun addBehavior(key: Any, behavior: AbstractBehavior) {
    behaviors.put(key, behavior)
  }

  /**
   * Returns if the [AbstractBehavior] with the given [key] is active.
   *
   * @param key The key of the [AbstractBehavior] to check.
   * @return If the [AbstractBehavior] with the given [key] is active.
   */
  fun isBehaviorActive(key: Any) = activeBehaviors.contains(key)

  /** @see [isBehaviorActive(Iterable<Any>)] */
  fun isAnyBehaviorActive(vararg keys: Any) = isAnyBehaviorActive(keys.asIterable())

  /**
   * Returns if any of the [AbstractBehavior]s with the given [keys] are active.
   *
   * @param keys The keys of the [AbstractBehavior]s to check.
   * @return If any of the [AbstractBehavior]s with the given [keys] are active.
   */
  fun isAnyBehaviorActive(keys: Iterable<Any>) = keys.any { isBehaviorActive(it) }

  /** @see [areAllBehaviorsActive(Iterable<Any>)] */
  fun areAllBehaviorsActive(vararg keys: Any) = areAllBehaviorsActive(keys.asIterable())

  /**
   * Returns if all of the [AbstractBehavior]s with the given [keys] are active.
   *
   * @param keys The keys of the [AbstractBehavior]s to check.
   * @return If all of the [AbstractBehavior]s with the given [keys] are active.
   */
  fun areAllBehaviorsActive(keys: Iterable<Any>) = keys.all { isBehaviorActive(it) }

  /**
   * Sets the [AbstractBehavior] with the given [key] to be active or inactive.
   *
   * @param key The key of the [AbstractBehavior] to set.
   * @param active If the [AbstractBehavior] should be active.
   */
  internal fun setActive(key: Any, active: Boolean) =
      if (active) activeBehaviors.add(key) else activeBehaviors.remove(key)

  /** Clears the list of active [AbstractBehavior]s. All behaviors are reset. */
  override fun reset() {
    activeBehaviors.clear()
    behaviors.values().forEach { it.reset() }
  }
}
