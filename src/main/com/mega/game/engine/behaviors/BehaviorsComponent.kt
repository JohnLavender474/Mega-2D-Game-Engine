package com.mega.game.engine.behaviors

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.components.IGameComponent
import java.util.function.BiFunction

/** A [IGameComponent] that manages a collection of [IBehavior]s. */
class BehaviorsComponent() : IGameComponent {

    val behaviors = OrderedMap<Any, IBehavior>()
    val allowedBehaviors = ObjectMap<Any, Boolean>()

    /**
     * Creates a [BehaviorsComponent] with the given [behaviors].
     *
     * @param _behaviors The [IBehavior]s to add to this [BehaviorsComponent].
     */
    constructor(vararg _behaviors: GamePair<Any, IBehavior>) : this() {
        _behaviors.forEach { addBehavior(it.first, it.second) }
    }

    /**
     * Creates a [BehaviorsComponent] with the given [behaviors].
     *
     * @param _behaviors The [IBehavior]s to add to this [BehaviorsComponent].
     */
    constructor(_behaviors: Iterable<GamePair<Any, IBehavior>>) : this() {
        _behaviors.forEach { addBehavior(it.first, it.second) }
    }

    /**
     * Creates a [BehaviorsComponent] with the given [behaviors].
     *
     * @param _behaviors The [IBehavior]s to add to this [BehaviorsComponent].
     */
    constructor(_behaviors: OrderedMap<Any, IBehavior>) : this() {
        _behaviors.forEach { addBehavior(it.key, it.value) }
    }

    /**
     * Returns the [IBehavior] associated with the given [key] or null if no [IBehavior] is associated with the [key].
     *
     * @param key The key of the [IBehavior] to get.
     * @return The [IBehavior] associated with the given [key] or null if no [IBehavior] is associated with the [key].
     */
    fun getBehavior(key: Any): IBehavior? = behaviors.get(key)

    /**
     * Adds a [IBehavior] to this [BehaviorsComponent] with the given [key]. If an [IBehavior] already
     * exists with the given [key], it will be overwritten. Insertion order is preserved via an [OrderedMap]. The
     * behavior will be allowed by default, i.e. [isBehaviorAllowed] will return true with the key.
     *
     * @param key The key to associate with the [IBehavior].
     * @param behavior The [IBehavior] to add.
     */
    fun addBehavior(key: Any, behavior: IBehavior) {
        behaviors.put(key, behavior)
        allowedBehaviors.put(key, true)
    }

    /**
     * Sets if the behavior should be allowed. If [allowed] is set to false and the behavior is currently active, then
     * [IBehavior.end] will be called on the behavior immediately. The specified [key] must be associated with a
     * behavior or else an [IllegalArgumentException] will be thrown.
     *
     * @param key the key of the behavior
     * @param allowed if the behavior should be allowed
     * @throws IllegalArgumentException
     */
    fun setBehaviorAllowed(key: Any, allowed: Boolean) {
        if (!behaviors.containsKey(key)) throw IllegalArgumentException("Key must be associated to an already added behavior")
        allowedBehaviors.put(key, allowed)
        if (!allowed) {
            val behavior = behaviors.get(key)
            if (behavior.isActive()) behavior.end()
        }
    }

    /**
     * Sets if each behavior should be allowed based on the return value of the function. See [setBehaviorAllowed].
     *
     * @param function the function which returns the value designating if the behavior should be allowed.
     */
    fun setBehaviorsAllowed(function: (Any, IBehavior) -> Boolean) {
        behaviors.forEach { entry ->
            val key = entry.key
            val behavior = entry.value
            val allowed = function.invoke(key, behavior)
            setBehaviorAllowed(key, allowed)
        }
    }

    /**
     * Convenience method for [setBehaviorsAllowed] that accepts a [BiFunction].
     *
     * @param function the function which returns the value designating if the behavior should be allowed.
     * @see [setBehaviorsAllowed]
     */
    fun setBehaviorsAllowed(function: BiFunction<Any, IBehavior, Boolean>) {
        setBehaviorsAllowed { key, behavior -> function.apply(key, behavior) }
    }

    /**
     * Sets if each of the specified behaviors should be allowed. See [setBehaviorAllowed].
     *
     * @param keys the keys of the behaviors to change
     * @param allowed if the behaviors should be allowed
     * @throws IllegalArgumentException
     */
    fun setBehaviorsAllowed(keys: Iterable<Any>, allowed: Boolean) {
        keys.forEach { key ->
            setBehaviorAllowed(key, allowed)
        }
    }

    /**
     * Sets if each behavior should be allowed. See [setBehaviorAllowed].
     *
     * @param allowed if behaviors should be allowed
     */
    fun setAllBehaviorsAllowed(allowed: Boolean) {
        behaviors.forEach { entry ->
            val key = entry.key
            setBehaviorAllowed(key, allowed)
        }
    }

    /**
     * Returns if the behavior is allowed.
     *
     * @param key The key of the behavior
     * @return If the behavior is allowed
     */
    fun isBehaviorAllowed(key: Any): Boolean = allowedBehaviors.containsKey(key) && allowedBehaviors.get(key)

    /**
     * Returns if the [IBehavior] with the given [key] is active.
     *
     * @param key The key of the [IBehavior] to check.
     * @return If the [IBehavior] with the given [key] is active.
     */
    fun isBehaviorActive(key: Any) = behaviors.get(key)?.isActive() ?: false

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

    /** Calls [IBehavior.reset] for each active behavior. */
    override fun reset() = behaviors.forEach {
        if (isBehaviorActive(it.key)) it.value.reset()
    }
}
