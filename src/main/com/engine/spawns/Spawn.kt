package com.engine.spawns

import com.engine.common.objects.Properties
import com.engine.entities.IGameEntity

/**
 * A spawn is a combination of an entity and properties.
 *
 * @param entity the entity to spawn
 * @param properties the properties to apply to the entity
 */
data class Spawn(val entity: IGameEntity, val properties: Properties)
