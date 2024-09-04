package com.mega.game.engine

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Queue
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.common.objects.MutableOrderedSet
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.entities.GameEntityState
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/**
 * The game engine class accepts as an argument an array of systems. On each call to [update], entities are
 * spawned, destroyed, and registered to or unregistered from systems. The systems can be passed in as an array;
 * otherwise a new array is instantiated. The [GameEntityState] object owned by each [IGameEntity] is modified by
 * the game engine, i.e. when an entity is initialized, then [GameEntityState.initialized] is set to true. Below are
 * the rules regarding state fields.
 * - When an entity is spawned via the [GameEngine.spawn] method, it is added to the [entities] set.
 * - When an entity is spawned via the engine spawn method, then if [GameEntityState.initialized] is false, then the
 * entity's [IGameEntity.init] method is called before the entity's [IGameEntity.onSpawn] and the initialized state
 * field is set to true.
 * - When an entity is spawned via the engine spawn method and [GameEntityState.initialized] is true, then the
 * [IGameEntity.init] is not called, and only [IGameEntity.onSpawn] is called.
 * - When an entity is spawned, [GameEntityState.spawned] is set to true. This marks the entity as "spawned" or "alive".
 * This value can be changed elsewhere, and it will have no effect on the entity's state in the game engine. However, it
 * is recommended not to alter the values in [GameEntityState] and leave modifications solely to the [GameEngine].
 * - When an entity is destroyed via the [GameEngine.destroy] method, then the [IGameEntity.onDestroy] method is called
 * and the [GameEntityState.spawned] field is set to false. Note that the [GameEntityState.initialized] field is left
 * true (assuming it has not been altered elsewhere). This is because the [IGameEntity.init] is not meant to be called
 * every time the entity is spawned.
 * - When an entity is destroyed via the [GameEngine.destroy] method, it is removed from the [entities] set.
 * @property entities the entities in this game engine
 * @property systems the systems in this game engine
 * @see GameSystem
 */
class GameEngine(var systems: Array<GameSystem> = Array()) : Updatable, Resettable {

    companion object {
        const val TAG = "GameEngine"
    }

    var updating = false
        private set

    private val entities = MutableOrderedSet<IGameEntity>()
    private val entitiesToSpawn = Queue<Pair<IGameEntity, Properties>>()
    private val entitiesToKill = Queue<IGameEntity>()
    private var reset = false

    /**
     * Returns true if this game engine contains the entity, otherwise false. The game engine "contains" the entity
     * once it is spawned. If the [containedIfQueuedToSpawn] parameter is true (it is false by default if it is not
     * specified), then this method will also return true if the entity is currently queued to be spawned.
     *
     * @param entity the entity to check if it is contained in this game engine
     * @param containedIfQueuedToSpawn will cause the method to return true if the entity is currently queued to spawn
     * @return if this game engine contains the entity, otherwise false
     */
    fun contains(entity: IGameEntity, containedIfQueuedToSpawn: Boolean = false) =
        entities.contains(entity) || (containedIfQueuedToSpawn && entitiesToSpawn.any { it.first == entity })

    /**
     * Returns an immutable view of the spawned entities in this game engine.
     *
     * @return an immutable view of the spawned entities in this game engine
     */
    fun getSpawnedEntities() = ImmutableCollection(entities)

    /**
     * If the result of [IGameEntity.canSpawn] is true for the given entity, then it is queued to be spawned and true
     * is returned. Otherwise, false is returned.
     *
     * @param entity the [IGameEntity] to spawn
     * @param spawnProps the [Properties] to spawn the [IGameEntity] with; defaults to an empty [Properties] object
     * @return true if the entity is queued to be spawned, otherwise false
     */
    fun spawn(entity: IGameEntity, spawnProps: Properties = Properties()) = if (entity.canSpawn()) {
        entitiesToSpawn.addLast(entity to spawnProps)
        true
    } else false

    private fun spawnNow(entity: IGameEntity, spawnProps: Properties) {
        entities.add(entity)
        if (!entity.gameEntityState.initialized) initialize(entity)
        entity.onSpawn(spawnProps)
        entity.gameEntityState.spawned = true
        updateSystemMembershipsFor(entity)
    }

    private fun initialize(entity: IGameEntity) {
        entity.init()
        entity.gameEntityState.initialized = true
    }

    /**
     * If the value of [GameEntityState.spawned] is true for the entity's state field, then the entity is queued
     * to be destroyed and true is returned. Otherwise, false is returend.
     *
     * @param entity the entity to destroy
     * @return true if the entity is queued to be destroyed, otherwise false
     */
    fun destroy(entity: IGameEntity) = if (entity.gameEntityState.spawned) {
        entitiesToKill.addLast(entity)
        true
    } else false

    private fun destroyNow(entity: IGameEntity) {
        entities.remove(entity)
        entity.gameEntityState.spawned = false
        systems.forEach { s -> s.remove(entity) }
        entity.components.forEach { it.value.reset() }
        entity.onDestroy()
    }

    /**
     * Updates the entity's system memberships by adding the entity to systems that it qualifies for and removing it
     * from systems that it does not qualify for.
     *
     * @param entity the entity to update system memberships for
     */
    fun updateSystemMembershipsFor(entity: IGameEntity) =
        systems.forEach { system -> if (system.qualifies(entity)) system.add(entity) else system.remove(entity) }

    /**
     * [IGameEntity]s that are dead will be destroyed. [IGameEntity]s that are spawned will be added.
     *
     * @param delta the time in seconds since the last update
     */
    override fun update(delta: Float) {
        updating = true
        while (!entitiesToSpawn.isEmpty) {
            val (entity, spawnProps) = entitiesToSpawn.removeFirst()
            spawnNow(entity, spawnProps)
        }
        while (!entitiesToKill.isEmpty) {
            val entity = entitiesToKill.removeFirst()
            destroyNow(entity)
        }
        systems.forEach { it.update(delta) }
        updating = false
        if (reset) reset()
    }

    /**
     * Resets the [GameEngine]. This will destroy all [IGameEntity]s that have [GameEntityState.spawned] as true, clear
     * all entities from the game engine, and reset all [GameSystem]s. If the game engine is updating, then the reset
     * operations will not be run until the next call to [update]. Otherwise, the reset operations are run immediately.
     */
    override fun reset() {
        reset = if (updating) true
        else {
            entities.filter { it.gameEntityState.spawned }.forEach { destroyNow(it) }
            entities.clear()
            entitiesToSpawn.clear()
            entitiesToKill.clear()
            systems.forEach { it.reset() }
            false
        }
    }
}
