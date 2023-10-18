package com.engine.damage

import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A component that handles damaging other entities.
 *
 * @param entity The entity.
 * @param damager The damager.
 */
class DamagerComponent(override val entity: IGameEntity, val damager: IDamager) : IGameComponent
