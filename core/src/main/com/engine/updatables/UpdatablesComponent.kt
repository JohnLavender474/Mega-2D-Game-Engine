package com.engine.updatables

import com.engine.GameComponent
import com.engine.common.interfaces.Updatable

/**
 * The updatable component. Contains all the updatables for an entity.
 *
 * @param updatables The updatables
 */
class UpdatablesComponent(val updatables: ArrayList<Updatable> = ArrayList()) : GameComponent
