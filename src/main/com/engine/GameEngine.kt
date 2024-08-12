package com.engine

import com.badlogic.gdx.utils.OrderedSet
import com.badlogic.gdx.utils.Queue
import com.engine.common.objects.Properties
import com.engine.entities.IGameEntity
import com.engine.systems.IGameSystem
import java.util.function.Consumer

/**
 * The main class of the game. It contains all of the [IGameSystem]s and [IGameEntity]s.
 *
 * @param systems the [IGameSystem]s in this [GameEngine]
 */
class GameEngine(override val systems: Iterable<IGameSystem>) : IGameEngine {

    companion object {
        const val TAG = "GameEngine"
    }

    internal val entities = OrderedSet<IGameEntity>()
    internal val entitiesToAdd = Queue<Pair<IGameEntity, Properties>>()

    private var reset = false
    private var updating = false

    /**
     * Creates a [GameEngine] with the given [IGameSystem]s.
     *
     * @param systems the [IGameSystem]s to add to this [GameEngine]
     */
    constructor(vararg systems: IGameSystem) : this(systems.asIterable())

    /**
     * Performs the action on each entity.
     *
     * @param action the action to perform
     */
    fun forEachEntity(action: (IGameEntity) -> Unit) = entities.forEach { action(it) }

    /**
     * Performs the action on each entity.
     *
     * @param action the action to perform
     */
    fun forEachEntity(action: Consumer<IGameEntity>) = entities.forEach { action.accept(it) }

    /**
     * Finds all [IGameEntity]s that match the given [predicate] and returns them as an [OrderedSet].
     *
     * @param predicate the predicate to match [IGameEntity]s against
     * @return an [OrderedSet] of [IGameEntity]s that match the given [predicate]
     */
    fun findEntitiesBy(predicate: (IGameEntity) -> Boolean): OrderedSet<IGameEntity> {
        val set = OrderedSet<IGameEntity>()
        entities.forEach { if (predicate(it)) set.add(it) }
        return set
    }

    /**
     * Finds the first [IGameEntity] that matches the given [predicate] and returns it. If no
     * [IGameEntity] matches the given [predicate], then null is returned.
     *
     * @param predicate the predicate to match [IGameEntity]s against
     * @return the first [IGameEntity] that matches the given [predicate], or null if no [IGameEntity]
     *   matches the given [predicate]
     */
    fun findFirstEntityBy(predicate: (IGameEntity) -> Boolean): IGameEntity? {
        for (entity in entities) if (predicate(entity)) return entity
        return null
    }

    /**
     * [IGameEntity]s passed into the [spawn] method will be added to a queue, and will not truly be
     * spawned until the next call to [update].
     *
     * @param entity the [IGameEntity] to spawn
     * @param spawnProps the [Properties] to spawn the [IGameEntity] with
     */
    override fun spawn(entity: IGameEntity, spawnProps: Properties): Boolean {
        entitiesToAdd.addLast(entity to spawnProps)
        return true
    }

    /**
     * [IGameEntity]s that are dead will be destroyed. [IGameEntity]s that are spawned will be added.
     *
     * @param delta the time in seconds since the last update
     */
    override fun update(delta: Float) {
        updating = true

        // add new entities
        while (!entitiesToAdd.isEmpty) {
            val (entity, spawnProps) = entitiesToAdd.removeFirst()

            entities.add(entity)
            entity.spawn(spawnProps)

            systems.forEach { system ->
                if (system.qualifies(entity)) system.add(entity)
            }
        }

        // remove and destroy dead entities
        val eIter = entities.iterator()
        while (eIter.hasNext) {
            val e = eIter.next()
            if (!e.dead) continue

            systems.forEach { s -> s.remove(e) }

            e.onDestroy()
            eIter.remove()
        }

        // update systems
        systems.forEach { it.update(delta) }

        updating = false

        if (reset) reset()
    }

    /**
     * Resets the [GameEngine]. This will destroy all [IGameEntity]s and reset all [IGameSystem]s in
     * the next update cycle.
     */
    override fun reset() {
        reset =
            if (updating) true
            else {
                entities.forEach { e ->
                    e.kill()
                    e.onDestroy()
                }
                entities.clear()
                entitiesToAdd.clear()

                systems.forEach { it.reset() }

                false
            }
    }
}
