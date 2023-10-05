package com.engine

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedSet
import com.engine.common.objects.Properties
import com.engine.entities.IGameEntity
import com.engine.systems.IGameSystem

/**
 * The main class of the game. It contains all of the [IGameSystem]s and [IGameEntity]s.
 *
 * @property systems the [IGameSystem]s in this [GameEngine]
 * @property autoSetAlive whether to automatically set [IGameEntity]s to alive ([IGameEntity.dead]
 *   set to false) every time they are spawned. If this is false, then the [spawn] method of the
 *   [IGameEntity] should set [IGameEntity.dead] to false (in case successful spawning is
 *   conditional).
 */
class GameEngine(override val systems: Iterable<IGameSystem>, var autoSetAlive: Boolean = true) :
    IGameEngine {

  internal val entities = OrderedSet<IGameEntity>()
  internal val entitiesToAdd = Array<Pair<IGameEntity, Properties>>()

  private var purge = false
  private var updating = false

  /**
   * Creates a [GameEngine] with the given [IGameSystem]s.
   *
   * @param autoSetAlive whether to automatically set [IGameEntity]s to alive ([IGameEntity.dead]
   *   set to false) every time they are spawned. If this is false, then the [spawn] method of the
   *   [IGameEntity] should set [IGameEntity.dead] to false (in case successful spawning is
   *   conditional).
   * @param systems the [IGameSystem]s to add to this [GameEngine]
   */
  constructor(
      autoSetAlive: Boolean = true,
      vararg systems: IGameSystem
  ) : this(systems.asIterable(), autoSetAlive)

  override fun spawn(entity: IGameEntity, spawnProps: Properties): Boolean {
    entitiesToAdd.add(entity to spawnProps)
    return true
  }

  override fun update(delta: Float) {
    updating = true

    // add entities if necessary
    entitiesToAdd.forEach {
      val (entity, spawnProps) = it
      entities.add(entity)
      entity.spawn(spawnProps)
      entity.getComponents().forEach { c -> c.reset() }
      systems.forEach { s -> s.add(entity) }

      // set alive if necessary
      if (autoSetAlive) entity.dead = false
    }
    entitiesToAdd.clear()

    // remove dead entities
    entities
        .filter { it.dead }
        .forEach {
          entities.remove(it)
          systems.forEach { s -> s.remove(it) }
          it.destroy()
        }

    // update systems
    systems.forEach { it.update(delta) }

    updating = false
    if (purge) purge()
  }

  private fun purge() {
    if (updating) {
      purge = true
    } else {
      entities.forEach {
        it.destroy()
        it.dead = true
      }
      entities.clear()
      systems.forEach { it.purge() }
      purge = false
    }
  }

  override fun reset() = purge()
}
