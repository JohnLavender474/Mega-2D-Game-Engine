package com.engine

import com.engine.common.objects.Properties
import com.engine.entities.GameEntity

/**
 * A simple [GameEntity] for testing purposes. Dead is set to false by default.
 *
 * @see GameEntity
 */
class SimpleMockEntity : GameEntity() {

  init {
    dead = false
  }

  override fun spawn(spawnProps: Properties) {}

  override fun destroy() {}

  override fun reset() {}
}
