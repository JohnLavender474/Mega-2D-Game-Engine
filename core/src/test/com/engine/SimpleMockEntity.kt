package com.engine

import com.engine.common.objects.Properties

class SimpleMockEntity : GameEntity() {
  override fun spawn(spawnProps: Properties) {}

  override fun runOnDeath() {}

  override fun reset() {}
}
