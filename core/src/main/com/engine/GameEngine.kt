package com.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.OrderedSet
import com.badlogic.gdx.utils.Queue
import com.engine.common.objects.Properties
import com.engine.entities.IGameEntity
import com.engine.systems.IGameSystem

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
   * @inheritDoc
   *
   * [IGameEntity]s passed into the [spawn] method will be added to a queue, and will not truly be
   * spawned until the next call to [update].
   *
   * @param entity the [IGameEntity] to spawn
   * @param spawnProps the [Properties] to spawn the [IGameEntity] with
   */
  override fun spawn(entity: IGameEntity, spawnProps: Properties): Boolean {
    Gdx.app.debug(TAG, "spawn(): Spawning entity: $entity")
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
        if (system.qualifies(entity)) {
          Gdx.app.debug(
              TAG,
              "update(): Adding entity [$entity] to system[${system::class.simpleName}]")
          system.add(entity)
        }
      }
    }

    // remove and destroy dead entities
    val eIter = entities.iterator()
    while (eIter.hasNext) {
      val e = eIter.next()
      if (!e.dead) continue

      systems.forEach { s ->
        Gdx.app.debug(TAG, "update(): Removing entity [$e] from system [${s::class.simpleName}]")
        s.remove(e)
      }

      Gdx.app.debug(TAG, "update(): Destroying entity: $e")
      e.onDestroy()
      eIter.remove()
    }

    // update systems
    systems.forEach { it.update(delta) }

    updating = false

    if (reset) reset()
  }

  /** Resets the [GameEngine]. This will destroy all [IGameEntity]s and reset all [IGameSystem]s. */
  override fun reset() {
    reset =
        if (updating) {
          true
        } else {
          entities.forEach { e ->
            Gdx.app.debug(TAG, "reset(): Destroying entity: $e")
            e.onDestroy()
          }
          entities.clear()
          entitiesToAdd.clear()

          systems.forEach {
            Gdx.app.debug(TAG, "reset(): Resetting system: ${it::class.simpleName}")
            it.reset()
          }
          false
        }
  }
}
