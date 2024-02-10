package com.engine.entities.contracts

import com.engine.behaviors.AbstractBehavior
import com.engine.behaviors.BehaviorsComponent
import com.engine.entities.IGameEntity

/**
 * An interface for [IGameEntity]s that have [AbstractBehavior]s.
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
     * Returns if the [AbstractBehavior] with the given [key] is active.
     *
     * @param key The key of the [AbstractBehavior] to check.
     * @return If the [AbstractBehavior] with the given [key] is active.
     */
    fun isBehaviorActive(key: Any) = getBehaviorsComponent()?.isBehaviorActive(key) ?: false

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
     * Forces the [AbstractBehavior] with the given [key] to quit. This will force the [AbstractBehavior] to disregard
     * the result of its [AbstractBehavior.evaluate] method for one update cycle and end the [AbstractBehavior].
     *
     * @param key The key of the [AbstractBehavior] to force quit.
     */
    fun forceQuitBehavior(key: Any) = getBehaviorsComponent()?.forceQuitBehavior(key)
}
