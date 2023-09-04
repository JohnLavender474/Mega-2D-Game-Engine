package com.engine

import com.engine.common.objects.Properties

/**
 * The main class of the game. It contains all of the [GameSystem]s and [GameEntity]s.
 *
 * @property systems the [GameSystem]s in this [GameEngine]
 * @property autoSetAlive whether to automatically set [GameEntity]s to alive ([GameEntity.dead] set
 *   to false) every time they are spawned
 */
class GameEngine(override val systems: Collection<GameSystem>, var autoSetAlive: Boolean = true) :
    IGameEngine {

  internal val entities = HashSet<GameEntity>()
  internal val entitiesToAdd = ArrayList<Pair<GameEntity, Properties>>()

  private var purge = false
  private var updating = false

  override fun spawn(entity: GameEntity, spawnProps: Properties) =
      entitiesToAdd.add(entity to spawnProps)

  override fun update(delta: Float) {
    updating = true

    // add entities if necessary
    entitiesToAdd.forEach {
      val (entity, spawnProps) = it
      entities.add(entity)
      entity.spawn(spawnProps)
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
          it.runOnDeath()
        }

    // update systems
    systems.forEach { it.update(delta) }

    // purge if necessary
    if (purge) {
      purge()
      purge = false
    }

    updating = false
  }

  fun purge() {
    entities.forEach {
      it.runOnDeath()
      it.dead = true
    }
    entities.clear()
    systems.forEach { it.purge() }
  }

  override fun reset() {
    if (updating) {
      purge = true
    } else {
      purge()
    }
  }
}
