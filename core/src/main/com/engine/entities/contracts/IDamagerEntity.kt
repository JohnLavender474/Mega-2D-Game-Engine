package com.engine.entities.contracts

import com.engine.damage.IDamager
import com.engine.entities.IGameEntity

/** An entity that can damage other entities. */
interface IDamagerEntity : IGameEntity, IDamager
