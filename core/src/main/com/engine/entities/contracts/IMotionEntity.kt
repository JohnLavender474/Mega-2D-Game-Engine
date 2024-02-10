package com.engine.entities.contracts

import com.engine.entities.IGameEntity
import com.engine.motion.IMotion
import com.engine.motion.MotionComponent

/**
 * An entity that can be moved by a motion value. The motion value is obtained from an [IMotion]
 * that is stored in a [MotionComponent].
 */
interface IMotionEntity : IGameEntity {

    /**
     * Returns the [MotionComponent] of this entity. Throws exception if no [MotionComponent] has been
     * added.
     *
     * @return the [MotionComponent] of this entity
     */
    fun getMotionComponent() = getComponent(MotionComponent::class)!!

    /**
     * Returns the motions of this entity.
     *
     * @return the motions of this entity
     */
    fun getMotionDefinitions() = getMotionComponent().definitions

    /**
     * Returns the [IMotion] associated with the given key.
     *
     * @param key the key to get the [IMotion] for
     * @return the [IMotion] associated with the given key
     */
    fun getMotionDefinition(key: Any): MotionComponent.MotionDefinition? = getMotionDefinitions().get(key)

    /**
     * Returns the [IMotion] associated with the given key.
     *
     * @param key the key to get the [IMotion] for
     * @return the [IMotion] associated with the given key
     */
    fun getMotion(key: Any): IMotion? = getMotionDefinition(key)?.motion

    /**
     * Adds a [IMotion] to this entity.
     *
     * @param key the key to associate with the [IMotion]
     * @param definition the [IMotion] and function pair
     * @return the prior [IMotion] value if any, or null
     */
    fun putMotionDefinition(key: Any, definition: MotionComponent.MotionDefinition) =
        getMotionComponent().put(key, definition)

    /**
     * Removes a [IMotion] from this entity.
     *
     * @param key the key to remove
     * @return the [IMotion] that was removed, or null
     */
    fun removeMotionDefinition(key: Any): MotionComponent.MotionDefinition? = getMotionDefinitions().remove(key)

    /** Clears all motions from this entity. */
    fun clearMotionDefinitions() = getMotionDefinitions().clear()

    /** Resets the motions in this entity. */
    fun resetMotionComponent() = getMotionComponent().reset()
}
