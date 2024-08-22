package com.engine

import com.badlogic.gdx.utils.OrderedSet
import com.badlogic.gdx.utils.Queue
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.Properties
import com.engine.entities.IGameEntity
import com.engine.systems.IGameSystem

/**
 * The main class of the game. It contains all of the [IGameSystem]s and [IGameEntity]s.
 *
 * @param systems the [IGameSystem]s in this [GameEngine]
 */
class GameEngine(val systems: Iterable<IGameSystem>) : Updatable, Resettable {

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
     * [IGameEntity]s passed into the [spawn] method will be added to a queue, and will not truly be
     * spawned until the next call to [update].
     *
     * @param entity the [IGameEntity] to spawn
     * @param spawnProps the [Properties] to spawn the [IGameEntity] with
     */
    fun spawn(entity: IGameEntity, spawnProps: Properties): Boolean {
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

            if (!entity.canSpawn()) continue

            entities.add(entity)
            entity.spawn(spawnProps)

            systems.forEach { system -> if (system.qualifies(entity)) system.add(entity) }
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
