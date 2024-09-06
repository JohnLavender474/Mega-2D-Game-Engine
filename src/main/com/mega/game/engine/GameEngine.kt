package com.mega.game.engine

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.Queue
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.common.objects.MutableOrderedSet
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.SimpleQueueSet
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/**
 * The GameEngine class manages the entities and systems in the game. It handles spawning and destroying entities,
 * updating systems, and managing the state of the game. Entities can be queued to spawn or be destroyed in the update
 * cycle. Each entity can be part of multiple systems, and the GameEngine is responsible for updating system memberships
 * as entities are added or removed.
 *
 * @property systems The systems registered with this game engine, which will be updated each cycle.
 */
class GameEngine(var systems: Array<GameSystem> = Array()) : Updatable, Resettable, Disposable {

    companion object {
        const val TAG = "GameEngine"
    }

    /**
     * A helper class for managing a queue of entities to be spawned. This ensures that entities are added in the correct
     * order and avoids duplication in the spawn queue.
     */
    internal class EntitiesToSpawn {

        private val queue = Queue<Pair<IGameEntity, Properties>>()
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
            queue.addLast(entity to spawnProps)
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
        internal fun poll(): Pair<IGameEntity, Properties> {
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

    /**
     * Checks if the game engine has been disposed. This value is true when [dispose] has been called.
     */
    var disposed = false
        private set

    private val entities = MutableOrderedSet<IGameEntity>()
    private val entitiesToSpawn = EntitiesToSpawn()
    private val entitiesToKill = SimpleQueueSet<IGameEntity>()

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
     * Returns an immutable view of all the entities currently managed by the game engine.
     *
     * @return An immutable collection of the entities in the game engine.
     */
    fun getEntities() = ImmutableCollection(entities)

    /**
     * Queues the given entity to be spawned in the next update cycle. The entity must be able to spawn, which is
     * determined by the result of its `canSpawn()` method.
     *
     * @param entity The entity to queue for spawning.
     * @param spawnProps The properties to use when spawning the entity.
     * @return True if the entity is successfully queued to spawn; False otherwise.
     */
    fun spawn(entity: IGameEntity, spawnProps: Properties = Properties()) = if (entity.canSpawn()) {
        entitiesToSpawn.add(entity, spawnProps)
        true
    } else false

    private fun spawnNow(entity: IGameEntity, spawnProps: Properties) {
        entities.add(entity)
        if (!entity.state.initialized) initialize(entity)
        entity.onSpawn(spawnProps)
        entity.state.spawned = true
        updateSystemMembershipsFor(entity)
    }

    private fun initialize(entity: IGameEntity) {
        entity.init()
        entity.state.initialized = true
    }

    /**
     * Queues the given entity to be destroyed in the next update cycle. The entity will be removed from the engine
     * and its state will be updated to indicate that it is no longer active.
     *
     * @param entity The entity to destroy.
     * @return True if the entity is successfully queued for destruction; False otherwise.
     */
    fun destroy(entity: IGameEntity) = if (entity.state.spawned) {
        entitiesToKill.add(entity)
        true
    } else false

    private fun destroyNow(entity: IGameEntity) {
        entities.remove(entity)
        entity.state.spawned = false
        systems.forEach { s -> s.remove(entity) }
        entity.components.forEach { it.value.reset() }
        entity.onDestroy()
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
        if (disposed) throw IllegalStateException("Cannot update game engine after is has been disposed")
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
    }

    /**
     * Resets the game engine by destroying all spawned entities, clearing the entities from the engine, and resetting
     * all systems. This method cannot be called while the engine is updating. If it is called during an update cycle,
     * an [IllegalStateException] will be thrown.
     *
     * @throws IllegalStateException If called during an update.
     */
    override fun reset() {
        if (updating) throw IllegalStateException("Cannot reset game engine while updating")
        entities.filter { it.state.spawned }.forEach { destroyNow(it) }
        entities.clear()
        entitiesToSpawn.clear()
        entitiesToKill.clear()
        systems.forEach { it.reset() }
    }

    /**
     * Disposes of the game engine by first resetting it, and then disposing each system. After disposal, no further
     * updates can occur, and any attempt to update will result in an [IllegalStateException].
     *
     * @throws IllegalStateException If called while the engine is updating or if the engine has already been disposed
     */
    override fun dispose() {
        if (disposed) throw IllegalStateException("Cannot dispose game engine after it has already been disposed")
        if (updating) throw IllegalStateException("Cannot dispose game engine while updating")
        reset()
        systems.forEach { it.dispose() }
        disposed = true
    }
}
