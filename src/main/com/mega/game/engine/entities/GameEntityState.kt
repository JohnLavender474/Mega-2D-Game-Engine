package com.mega.game.engine.entities

import com.mega.game.engine.GameEngine
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.Properties

/**
 * The properties for a [GameEntity], mapped to the entity in the [GameEngine] class.
 *
 * @property spawned if the entity is spawned
 * @property initialized if the entity is initialized
 */
class GameEntityState(var initialized: Boolean = false, var spawned: Boolean = false)

