package com.engine.damage

import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

class DamagerComponent(override val entity: IGameEntity, val damager: IDamager) : IGameComponent
