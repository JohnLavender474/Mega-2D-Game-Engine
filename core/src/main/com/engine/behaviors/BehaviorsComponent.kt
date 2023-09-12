package com.engine.behaviors

import com.engine.GameComponent

/** A [GameComponent] that manages a collection of [Behavior]s. */
class BehaviorsComponent : GameComponent {

  internal val behaviors = LinkedHashMap<String, Behavior>()

  private val activeBehaviors = HashSet<String>()

  /**
   * Adds a [Behavior] to this [BehaviorsComponent] with the given [key]. If a [Behavior] already
   * exists with the given [key], it will be overwritten. Insertion order of behaviors is preserved
   * via a [LinkedHashMap]. The [Behavior] will be inactive by default.
   *
   * @param key The key to associate with the [Behavior].
   * @param behavior The [Behavior] to add.
   */
  fun addBehavior(key: String, behavior: Behavior) = behaviors.put(key, behavior)

  /**
   * Returns if the [Behavior] with the given [key] is active.
   *
   * @param key The key of the [Behavior] to check.
   * @return If the [Behavior] with the given [key] is active.
   */
  fun isBehaviorActive(key: String) = activeBehaviors.contains(key)

  /**
   * Returns if any of the [Behavior]s with the given [keys] are active.
   *
   * @param keys The keys of the [Behavior]s to check.
   * @return If any of the [Behavior]s with the given [keys] are active.
   */
  fun isAnyBehaviorActive(keys: Collection<String>) = keys.any { isBehaviorActive(it) }

  /**
   * Returns if all of the [Behavior]s with the given [keys] are active.
   *
   * @param keys The keys of the [Behavior]s to check.
   * @return If all of the [Behavior]s with the given [keys] are active.
   */
  fun areAllBehaviorsActive(keys: Collection<String>) = keys.all { isBehaviorActive(it) }

  /**
   * Sets the [Behavior] with the given [key] to be active or inactive.
   *
   * @param key The key of the [Behavior] to set.
   * @param active If the [Behavior] should be active.
   */
  internal fun setActive(key: String, active: Boolean) {
    if (active) {
      activeBehaviors.add(key)
    } else {
      activeBehaviors.remove(key)
    }
  }

  /** Clears the list of active [Behavior]s. All behaviors are reset. */
  override fun reset() {
    activeBehaviors.clear()
    behaviors.values.forEach { it.reset() }
  }
}
