package com.mega.game.engine

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.Queue
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.common.objects.*
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/**
 * The [GameEngine] class manages the entities and systems in the game. It handles spawning and destroying entities,
 * updating systems, and managing the state of the game. Entities can be queued to be spawned or destroyed in the update
 * cycle using the [spawn] and [destroy] methods respectively. Each entity can be part of multiple systems, and the
 * engine is responsible for updating system memberships as entities are added or removed.
 *
 * When [spawn] is called, the entity is queued to be spawned. Usually, the queued entity is not processed until the
 * next call to [update]. However, it is possible for the entity to be spawned within the same update frame, especially
 * if the entity is queued to spawn while the spawn queue is being processed, i.e. if [spawn] is called for an entity
 * from another entity's [IGameEntity.onSpawn] method. When an entity is spawned, it is added to the game engine  and its
 * [IGameEntity.init] method is called if [IGameEntity.initialized] is false, its [IGameEntity.onSpawn] method is called,
 * and it is added to all systems that it qualifies for.
 *
 * When [destroy] is called, the entity is queued to be destroyed, usually for the next call to [update]. When the
 * entity is destroyed during the update cycle, it is removed from the game engine, its [IGameEntity.onDestroy] method
 * is called, its components are reset, and it's removed from all systems in this game engine.
 *
 * @property systems The systems registered with this game engine, which will be updated each cycle.
 * @property onQueueToSpawn Optional lambda to call at the moment when an entity is queued to be spawned
 * @property onQueueToDestroy Optional lambda to call at the moment when an entity is queued to be destroyed
 */
class GameEngine(
    var systems: Array<GameSystem> = Array(),
    var onQueueToSpawn: ((IGameEntity) -> Unit)? = null,
    var onQueueToDestroy: ((IGameEntity) -> Unit)? = null
) : Updatable, Resettable, Disposable {

    /**
     * A helper class for managing a queue of entities to be spawned. This ensures that entities are added in the correct
     * order and avoids duplication in the spawn queue.
     */
    internal class EntitiesToSpawn {

        private val queue = Queue<GamePair<IGameEntity, Properties>>()
        private val set = ObjectSet<IGameEntity>()

        /**
         * Queues the entity to be spawned if it is currently not contained in the set and returns true. Otherwise,
         * the entity is not added and false is returned.
         *
         * @param entity the entity to queue to spawn
         * @param spawnProps the spawn properties
         * @return true if the entity is queued, false otherwise
         */
        internal fun add(entity: IGameEntity, spawnProps: Properties) = if (contains(entity)) false
        else {
            queue.addLast(entity pairTo spawnProps)
            set.add(entity)
            true
        }

        /**
         * If the queue contains the entity.
         *
         * @param entity the entity to check
         * @return if the queue contains the entity
         */
        internal fun contains(entity: IGameEntity) = set.contains(entity)

        /**
         * If the queue is empty.
         *
         * @return if the queue is empty
         */
        internal fun isEmpty() = queue.isEmpty

        /**
         * Removes from the head of the queue a pair containing the [IGameEntity] and [Properties].
         *
         * @return a pair containing the entity and spawn properties
         */
        internal fun poll(): GamePair<IGameEntity, Properties> {
            val pair = queue.removeFirst()
            set.remove(pair.first)
            return pair
        }

        /**
         * Clears the queue.
         */
        internal fun clear() {
            queue.clear()
            set.clear()
        }
    }

    /**
     * Checks if the game engine is currently in an update cycle. This value is true while the update method is
     * running and is false otherwise.
     */
    var updating = false
        private set

    private val entities = MutableOrderedSet<IGameEntity>()
    private val entitiesToSpawn = EntitiesToSpawn()
    private val entitiesToKill = SimpleQueueSet<IGameEntity>()
    private var reset = false
    private var disposed = false

    /**
     * Checks if the specified entity is currently part of the game engine. This checks whether the entity is in the
     * active set of entities or queued for spawning.
     *
     * @param entity The entity to check.
     * @param containedIfQueuedToSpawn If true, the method also checks if the entity is queued to be spawned.
     * @return True if the entity is present in the engine; False otherwise.
     */
    fun contains(entity: IGameEntity, containedIfQueuedToSpawn: Boolean = false) =
        entities.contains(entity) || (containedIfQueuedToSpawn && entitiesToSpawn.contains(entity))

    /**
     * Queues the given entity to be spawned in the next update cycle. The entity must be able to spawn, which is
     * determined by the result of its `canSpawn()` method.
     *
     * @param entity The entity to queue for spawning.
     * @param spawnProps The properties to use when spawning the entity.
     * @return True if the entity is successfully queued to spawn; False otherwise.
     */
    fun spawn(entity: IGameEntity, spawnProps: Properties = Properties()) = if (entity.canSpawn(spawnProps)) {
        val queued = entitiesToSpawn.add(entity, spawnProps)
        if (queued) onQueueToSpawn?.invoke(entity)
        queued
    } else false

    private fun spawnNow(entity: IGameEntity, spawnProps: Properties) {
        entities.add(entity)
        if (!entity.initialized) initialize(entity)
        entity.onSpawn(spawnProps)
        updateSystemMembershipsFor(entity)
        entity.spawned = true
    }

    private fun initialize(entity: IGameEntity) {
        entity.init()
        entity.initialized = true
    }

    /**
     * Queues the given entity to be destroyed in the next update cycle. The entity will be removed from the engine
     * and its state will be updated to indicate that it is no longer active.
     *
     * @param entity The entity to destroy.
     * @return True if the entity is successfully queued for destruction; False otherwise.
     */
    fun destroy(entity: IGameEntity): Boolean {
        val shouldBeQueued = entitiesToKill.add(entity)
        if (shouldBeQueued) onQueueToDestroy?.invoke(entity)
        return shouldBeQueued
    }

    private fun destroyNow(entity: IGameEntity) {
        entities.remove(entity)
        systems.forEach { s -> s.remove(entity) }
        entity.components.forEach { it.value.reset() }
        entity.onDestroy()
        entity.spawned = false
    }

    /**
     * Updates the system memberships for the given entity. This adds the entity to any systems it qualifies for,
     * and removes it from systems for which it no longer qualifies.
     *
     * @param entity The entity to update system memberships for.
     */
    fun updateSystemMembershipsFor(entity: IGameEntity) =
        systems.forEach { system -> if (system.qualifies(entity)) system.add(entity) else system.remove(entity) }

    /**
     * Updates the game engine. Entities that are queued to be spawned or destroyed will be processed, and all systems
     * will be updated. The value of `updating` is set to true during this method call. If this method is called
     * after the engine has been disposed, an [IllegalStateException] will be thrown.
     *
     * @param delta The time in seconds since the last update.
     * @throws IllegalStateException If the engine has been disposed.
     */
    override fun update(delta: Float) {
        if (disposed) throw IllegalStateException("Cannot update game engine after it has been disposed")
        updating = true
        while (!entitiesToSpawn.isEmpty()) {
            val (entity, spawnProps) = entitiesToSpawn.poll()
            spawnNow(entity, spawnProps)
        }
        while (!entitiesToKill.isEmpty()) {
            val entity = entitiesToKill.remove()
            destroyNow(entity)
        }
        systems.forEach { it.update(delta) }
        updating = false
        if (reset) reset()
    }

    /**
     * Resets the game engine by destroying all spawned entities, clearing the entities from the engine, and resetting
     * all systems. If this method is called during an update, then the reset operation will be delayed until the end
     * of this update cycle. Otherwise, the reset logic is applied immediately.
     */
    override fun reset() {
        reset = if (updating) true
        else {
            entities.filter { it.spawned }.forEach { destroyNow(it) }
            entities.clear()
            entitiesToSpawn.clear()
            entitiesToKill.clear()
            systems.forEach { it.reset() }
            false
        }
    }

    /**
     * Disposes the [GameEngine]. This will immediately call [Disposable.dispose] on each [GameSystem].
     */
    override fun dispose() {
        entities.filter { it.spawned }.forEach { destroyNow(it) }
        entities.clear()
        entitiesToSpawn.clear()
        entitiesToKill.clear()
        systems.forEach { it.reset() }
        systems.forEach { it.dispose() }
        disposed = true
    }
}
