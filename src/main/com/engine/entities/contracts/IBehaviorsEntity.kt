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
    fun isBehaviorActive(key: Any) = getBehaviorsComponent()!!.isBehaviorActive(key)

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
    fun forceQuitBehavior(key: Any) = getBehaviorsComponent()!!.forceQuitBehavior(key)

    /**
     * Returns if the behavior is allowed.
     *
     * @param key the key of the behavior
     * @return if the behavior is allowed
     */
    fun isBehaviorAllowed(key: Any) = getBehaviorsComponent()!!.isBehaviorAllowed(key)

    /**
     * Sets if the behavior should be allowed.
     *
     * @param key the key of the behavior
     * @param allowed if the behavior should be allowed
     */
    fun setBehaviorAllowed(key: Any, allowed: Boolean) = getBehaviorsComponent()!!.setBehaviorAllowed(key, allowed)

    /**
     * Sets if behaviors should be allowed based on the function.
     *
     * @param function the function
     */
    fun setBehaviorsAllowed(function: (Any, AbstractBehavior) -> Boolean) =
        getBehaviorsComponent()!!.setBehaviorsAllowed(function)

    /**
     * Sets if the behaviors with the given keys should be allowed
     *
     * @params keys the keys of the behaviors
     * @param allowed if the behaviors should be allowed
     */
    fun setBehaviorsAllowed(keys: Iterable<Any>, allowed: Boolean) =
        getBehaviorsComponent()!!.setBehaviorsAllowed(keys, allowed)

    /**
     * Sets if all behaviors should be allowed
     *
     * @param allowed if all behaviors should be allowed
     */
    fun setAllBehaviorsAllowed(allowed: Boolean) = getBehaviorsComponent()!!.setAllBehaviorsAllowed(allowed)
}
