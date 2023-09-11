package com.engine.spawns

import com.engine.GameEntity
import com.engine.common.objects.Properties

/**
 * A spawn is a combination of an entity and properties.
 *
 * @param entity the entity to spawn
 * @param properties the properties to apply to the entity
 */
data class Spawn(val entity: GameEntity, val properties: Properties)
