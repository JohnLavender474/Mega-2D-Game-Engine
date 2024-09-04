package com.mega.game.engine.systems

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.mega.game.engine.GameEngine
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.common.objects.MutableOrderedSet
import com.mega.game.engine.components.IGameComponent
import com.mega.game.engine.entities.IGameEntity
import kotlin.reflect.KClass

/**
 * An abstract implementation of [GameSystem]. It contains a [componentMask] which determines which [IGameEntity]s it
 * processes. It also contains a [MutableCollection] of entities that it processes. The collection by default is a
 * [MutableOrderedSet], which ensures that each entity is not processed more than once per update and at the same time
 * retains insertion order; but the collection type can be changed by overriding the [entities] property. It is
 * recommended that the entities property NOT be modified outside the system class. Instead, the user should use the
 * [add] and [remove] methods.
 *
 * @param componentMask the [KClass]es of [IGameComponent]s that this [GameSystem] accepts
 * @param entities the [MutableCollection] of [IGameEntity]s that this [GameSystem] processes
 */
abstract class GameSystem(
    private val componentMask: ObjectSet<KClass<out IGameComponent>>,
    private val entities: MutableOrderedSet<IGameEntity> = MutableOrderedSet()
) : Updatable, Resettable {

    companion object {
        const val TAG = "GameSystem"
        private fun buildComponentMaskFromVarArgs(
            componentMask: kotlin.Array<out KClass<out IGameComponent>>
        ): ObjectSet<KClass<out IGameComponent>> {
            val set = ObjectSet<KClass<out IGameComponent>>()
            componentMask.forEach { set.add(it) }
            return set
        }
    }

    private val entitiesToAdd = Array<IGameEntity>()
    private val entitiesToRemove = Array<IGameEntity>()

    var on = true
    var updating = false
        private set

    constructor(vararg componentMask: KClass<out IGameComponent>) : this(buildComponentMaskFromVarArgs(componentMask))

    /**
     * Processes the given [IGameEntity]s. This method is called by the [update] method. Implementations of this method
     * should process the given entities. The entities are guaranteed to have all the [IGameComponent]s contained in
     * [componentMask]. The collection is immutable. To make changes to the underlying collection of entities, use the
     * [add] and [remove] methods. This is to prevent [ConcurrentModificationException]s and to ensure that the entities
     * are processed correctly.
     *
     * @param on whether this [GameSystem] is on
     * @param entities the [Collection] of [IGameEntity]s to process
     * @param delta the time in seconds since the last frame
     */
    internal abstract fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float)

    /**
     * Returns true if the entity is contained in this system, false otherwise.
     *
     * @param e the entity to check
     * @return true if the entity is contained in this system, false otherwise.
     */
    fun contains(e: IGameEntity) = entities.contains(e)

    /**
     * Removes the entity from this system if it is currently contained in this system. If the entity if contained in
     * this system and the system is currenlty updating, then the entity is queued to be removed on the next call to
     * [update] and 2 is returned. If the entity is contained and the system is not updating, then the entity is
     * removed immediately and 1 is returned. If the entity is not contained in this system, then nothing happens and
     * 0 is returned.
     *
     * @param e the entity to remove from this system
     * @return 2 if the entity is queued to be returned, 1 if the entity is removed immediately, otherwise 0
     */
    fun remove(e: IGameEntity) = if (contains(e)) {
        if (updating) {
            entitiesToRemove.add(e)
            2
        } else {
            entities.remove(e)
            1
        }
    } else 0

    /**
     * Adds the entity if it qualifies for this system. If the entity qualifies but the system is currently updating,
     * then the entity is queued to be added in the next call to [update] and 2 is returned. If the entity qualifies
     * and the system is not currenlty updating, then the entity is added immediately and 1 is returned. If the entity
     * does not qualify, then the entity is not added to the system and 0 is returned.
     *
     * @param e the entity to try to add to this system
     * @return 2 if the entity is queued to be added, 1 if the entity is added immediately, and 0 if the entity
     * disqualifies from being added
     */
    fun add(e: IGameEntity): Int = if (qualifies(e)) {
        if (updating) {
            entitiesToAdd.add(e)
            2
        } else {
            entities.add(e)
            1
        }
    } else 0

    /**
     * Returns true if the entity qualifies to be added to this system, otherwise false.
     *
     * @param e the enitty to check for qualification
     * @return if the entity qualifies to be added to this system
     */
    fun qualifies(e: IGameEntity) = componentMask.all { e.hasComponent(it) }

    /**
     * Updates this [GameSystem]. This method is final; custom update logic should be added in the [process] method.
     * This method is called by the [GameEngine] every frame if the system is added to the engine. Entities that do not
     * qualify are removed from this [GameSystem] before the [process] method is called.
     *
     * @param delta the time in seconds since the last frame
     * @see GameEngine
     */
    final override fun update(delta: Float) {
        updating = true

        entities.addAll(entitiesToAdd)
        entitiesToAdd.clear()

        entities.removeAll(entitiesToRemove)
        entitiesToRemove.clear()

        entities.removeAll { !qualifies(it) }
        process(on, ImmutableCollection(entities), delta)

        updating = false
    }

    /**
     * Clears all [IGameEntity]s from this [GameSystem] and resets it to its default state. This
     * method is called by the [GameEngine] when the game is reset.
     *
     * @see GameEngine
     * @see Resettable
     */
    override fun reset() {
        entities.clear()
        entitiesToAdd.clear()
    }
}
